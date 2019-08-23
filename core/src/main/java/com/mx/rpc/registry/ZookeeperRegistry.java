package com.mx.rpc.registry;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author mx
 * @date 2019/8/20 9:24 AM
 */
public class ZookeeperRegistry implements ServiceRegistry, ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;
    /**
     * 每次重试间隔
     */
    private static final int BASE_TIMEOUT = 2000;

    /**
     * zk重试策略(每 BASE_TIMEOUT ms重试一次, 最大重试次数 MAX_RETRIES 次)
     */
    private static final RetryPolicy RETRY_POLICY = new ExponentialBackoffRetry(BASE_TIMEOUT, MAX_RETRIES);

    /**
     * 连接超时时间
     */
    private static final int ZK_SESSION_TIMEOUT = 5000;
    /**
     * 会话超时时间
     */
    private static final int ZK_CONNECTION_TIMEOUT = 3000;
    /**
     * RPC注册在ZK上命名空间
     */
    private static final String ZK_REGISTRY_PATH = "/rpc";

    private static final String SEPARATOR = ":";

    private final String address;

    public ZookeeperRegistry(String ip, int port) {
        this.address = ip + this.SEPARATOR + port;
    }

    private CuratorFramework createCuratorFramework() {
        // 创建ZK客户端
        return CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(ZK_SESSION_TIMEOUT)
                .connectionTimeoutMs(ZK_CONNECTION_TIMEOUT)
                .retryPolicy(RETRY_POLICY)
                .build();
    }

    /**
     * 检查 serviceName 对应的ZK节点是否创建,如未创建则创建
     *
     * @param client
     * @param serviceName
     */
    public void checkNodeExists(CuratorFramework client, String serviceName) throws Exception {

        // 检查 registry 节点是否已经创建
        this.checkRegistryNodeExists(client);
        //检查 serviceName 节点
        String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;
        Stat stat = client.checkExists().forPath(servicePath);

        if (stat == null) {
            // 创建 serviceName 对应的节点
            client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath);
            logger.info("ZK 上创建 service 节点成功: {}", servicePath);
        }
    }

    @Override
    public String discover(String serviceName) {

        CuratorFramework client = createCuratorFramework();

        try {
            client.start();
            logger.info("ZK 服务已连接!");
            // 检查registry和service节点是否创建
            this.checkNodeExists(client, serviceName);
            // 获取service 服务节点
            String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;
            List<String> list = client.getChildren().forPath(servicePath);

            if (list == null || list.isEmpty()) {
                throw new ServiceNotFoundException(String.format("未发现任何节点注册的信息: %s", servicePath));
            }
            // 选取service节点
            String addr = this.doChooseServiceAddr(list);
            // 获取节点的值
            String serviceNodePath = servicePath + "/" + addr;
            return new String(client.getData().forPath(serviceNodePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 获取节点信息后,关闭ZK的连接
            client.close();
        }
    }

    @Override
    public void register(String serviceName, String serviceAddr) {

        try {
            CuratorFramework client = createCuratorFramework();
            // 连接服务器
            client.start();
            logger.info("ZK 服务已连接!");
            // 检查registry和service节点是否创建
            this.checkRegistryNodeExists(client);
            // 创建service节点
            String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;
            String serviceNodePath = serviceAddr + "/address-";
            String addr = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(serviceNodePath, serviceAddr.getBytes());

            logger.info("ZK 上创建 service服务地址节点成功: {}", addr);
        } catch (Exception e) {
            throw new RuntimeException("Rpc server 注册时发生异常!");
        }
    }

    /**
     * 根据返回的服务地址list,选取一个地址
     * @param addrList
     * @return
     */
    // TODO 可以增加多种选择模式模式,暂只写了<随机模式>
    public String doChooseServiceAddr(List<String> addrList) {

        if (addrList == null || addrList.isEmpty()) {
            return null;
        }
        String addr;
        if (addrList.size() == 1) {
            // 只有一个地址被注册
            addr = addrList.get(0);
            logger.info("选取唯一的一个节点: {}", addr);
        } else {
            // 可选择地址 > 1
            addr = addrList.get(ThreadLocalRandom.current().nextInt(addrList.size()));
            logger.info("选取随机的一个节点: {}", addr);
        }
        return addr;
    }

    /**
     * 检查 registry 节点是否已经创建, 如未创建则创建
     *
     * @param client
     */
    private void checkRegistryNodeExists(CuratorFramework client) {

        try {
            Stat stat = client.checkExists().forPath(ZK_REGISTRY_PATH);
            if (stat == null) {
                // 创建注册的根节点
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZK_REGISTRY_PATH);
                logger.info("ZK 上创建 registry 节点: {}", ZK_REGISTRY_PATH);
            }
        } catch (Exception e) {
            logger.error("ZK 上创建 registry 节点发生异常.");
            throw new RuntimeException("ZK 上创建 registry 节点发生异常.", e.getCause());
        }
    }
}

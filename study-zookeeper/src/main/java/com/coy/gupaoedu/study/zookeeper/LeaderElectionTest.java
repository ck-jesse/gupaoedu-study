package com.coy.gupaoedu.study.zookeeper;

import org.junit.Test;

/**
 * 基于zookeeper的leader选举 测试
 *
 * @author chenck
 * @date 2019/7/10 22:46
 */
public class LeaderElectionTest {

    public static final String connectString = "127.0.0.1:2181";

    ZookeeperClient curatorFramework = new ZookeeperClient(connectString);

    String path = "/leader";

    @Test
    public void leaderElectionTestA() {
        curatorFramework.leaderElection(path, "ClientA", 10000);
        while (true) {
        }
    }

    @Test
    public void leaderElectionTestB() {
        curatorFramework.leaderElection(path, "ClientB", 10000);
        while (true) {
        }
    }

    @Test
    public void leaderElectionTestC() {
        curatorFramework.leaderElection(path, "ClientC", 10000);
        while (true) {
        }
    }
}

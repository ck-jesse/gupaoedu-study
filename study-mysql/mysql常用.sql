-- 查看非交互式超时时间，如 JDBC 程序
show global variables like 'wait_timeout';
-- 查看交互式超时时间，如数据库工具
show global variables like 'interactive_timeout';
-- 查看 MySQL 当前有多少个连接
show global status like 'Thread%';

-- 查看 SQL 的执行状态
show PROCESSLIST;

-- 查看最大连接数，默认是 151 个，最大可以设置成 16384
show variables like 'max_connections';

-- 查看查询缓存参数
show variables like 'query_cache%';

-- 查看查询的开销
show status like 'Last_query_cost';

-- 查看优化器的追踪参数
SHOW VARIABLES LIKE 'optimizer_trace';

-- 查看存储引擎
show table status from hs_aftersale;

-- 查看数据库存放数据的路径
show variables like 'datadir';

-- 查看数据库对存储引擎的支持情况
show engines ;

-- 查看服务器状态 Buffer Pool
SHOW STATUS LIKE '%innodb_buffer_pool%';

-- 查看系统变量
SHOW VARIABLES like '%innodb_buffer_pool%';

-- 查看innodb_log参数
show variables like 'innodb_log%';

-- 查看log buffer 写入磁盘的参数
SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';

-- 查看数据和索引的大小
SELECT
	CONCAT( ROUND( SUM( DATA_LENGTH / 1024 / 1024 ), 2 ), 'MB' ) AS data_len,
	CONCAT( ROUND( SUM( INDEX_LENGTH / 1024 / 1024 ), 2 ), 'MB' ) AS index_len
FROM information_schema.TABLES WHERE table_schema = 'coy' AND table_name = 'user_info';
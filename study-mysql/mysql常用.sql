-- 查看非交互式超时时间，如 JDBC 程序 默认超时时间都是 28800 秒，8 小时
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

-- 查看表的索引
show indexes from user_info;

-- 查看事务提交方式 ON 表示自动提交，OFF 表示手动开启和提交
show variables like 'autocommit';

-- 修改事务提交方式 session 会话级别，global 全局级别
set session autocommit=off;

-- 查看锁的等待时间（默认是 50 秒）
show VARIABLES like 'innodb_lock_wait_timeout';

-- 查看行锁信息
-- Innodb_row_lock_current_waits：当前正在等待锁定的数量；
-- Innodb_row_lock_time ：从系统启动到现在锁定的总时间长度，单位 ms；
-- Innodb_row_lock_time_avg ：每次等待所花平均时间；
-- Innodb_row_lock_time_max：从系统启动到现在等待最长的一次所花的时间；
-- Innodb_row_lock_waits ：从系统启动到现在总共等待的次数。
show status like 'innodb_row_lock_%';

-- 查看当前运行的所有事务，还有具体的语句
-- 如果一个事务长时间持有锁不释放，可以 kill 事务对应的线程 ID，也就是 INNODB_TRX 表中的 trx_mysql_thread_id，例如执行 kill 4，kill 7，kill 8。
select * from information_schema.INNODB_TRX;


-- 查看慢查询参数
show variables like 'slow_query%';

-- 查看慢查询时间（默认10s超过这个时间就看做慢查询）
show variables like '%long_query%';

-- 设置慢查询 1 开启，0 关闭，重启后失效
set @@global.slow_query_log=1;
-- 设置慢查询时间 默认10 秒，另开一个窗口后才会查到最新值
set @@global.long_query_time=3;


-- 慢查询分析
-- 查询用时最多的 10 条慢 SQL（命令行语句）
-- Count 代表这个 SQL 执行了多少次；
-- Time 代表执行的时间，括号里面是累计时间；
-- Lock 表示锁定的时间，括号是累计；
-- Rows 表示返回的记录数，括号是累计。
mysqldumpslow -s t -t 10 -g 'select' /var/lib/mysql/VM-181-147-centos-slow.log


-- 查看SQL 语句执行时使用的资源
show profiles;
-- 查看是否开启
select @@profiling;
-- 设置开启 1表示开启
set @@profiling=1;

-- 查看最后一个 SQL 的执行详细信息，从中找出耗时较多的环节（没有 s）。
show profile;

-- 查看运行线程
show processlist;

-- 查看 select 次数
SHOW GLOBAL STATUS LIKE 'com_select';

-- 查看存储引擎的当前运行信息
show engine innodb status;

-- 将监控信息输出到错误信息 error log 中（15 秒钟一次）
show variables like 'innodb_status_output%';
SET GLOBAL innodb_status_output=ON;
SET GLOBAL innodb_status_output_locks=ON;

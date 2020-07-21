-- InnoDB 为每行记录都实现了两个隐藏字段：
-- DB_TRX_ID，6 字节：插入或更新行的最后一个事务的事务 ID，事务编号是自动递增的（我们把它理解为创建版本号，在数据新增或者修改为新数据的时候，记录当前事务 ID）。
-- DB_ROLL_PTR，7 字节：回滚指针（我们把它理解为删除版本号，数据被删除或记录为旧数据的时候，记录当前事务 ID）。
-- 我们把这两个事务 ID 理解为版本号。

-- 修改事务提交方式 session 会话级别，global 全局级别
set session autocommit=off;

-- 事务1
begin;
INSERT INTO `coy`.`user_info`(`Fname`, `Fopenid`, `Faddr`, `Ftext`) VALUES ('jesse', NULL, NULL, NULL);
INSERT INTO `coy`.`user_info`(`Fname`, `Fopenid`, `Faddr`, `Ftext`) VALUES ('coy', NULL, NULL, NULL);
commit;
-- 此时的数据，创建版本是当前事务 ID，删除版本为空：


-- 事务2
-- 查找规则：只能查找创建时间小于等于当前事务 ID 的数据，和删除时间大于当前事务 ID 的行（或未删除）。
begin;
-- (1) 第一次查询 事务1 insert 2条记录，读取到两条原始数据
select * from user_info ;
-- (2) 第二次查询 事务3 insert 1条记录，只能查到两条数据，
-- 不能查到在我的事务开始之后插入的数据，tom 的事务 ID 大于 2，所以还是只能查到两条数据。
select * from user_info ;
-- (3) 第三次查询 事务3 delete 1条记录，删除的数据依然可以查询出来
-- 在我事务开始之后删除的数据，所以 jack 依然可以查出来。所以还是这两条数据。
select * from user_info ;
-- (4) 第四次查询 事务5 update 1条记录，查询旧的两条数据
-- 因为更新后的数据 一盆鱼 创建版本大于 2，代表是在事务之后增加的，查不出来。
-- 而旧数据 coy 的删除版本大于 2，代表是在事务之后删除的，可以查出来。
select * from user_info ;
commit;

-- 事务3
-- 此时的数据，多了一条 tom，它的创建版本号是当前事务编号，3：
begin;
INSERT INTO `coy`.`user_info`(`Fname`, `Fopenid`, `Faddr`, `Ftext`) VALUES ('tom', NULL, NULL, NULL);
commit;

-- 第四个事务，删除数据，删除了 id=2 jack 这条记录：
-- 此时的数据，jack 的删除版本被记录为当前事务 ID，4，其他数据不变：
begin;
delete from user_info where Fpkid=2;
commit;

-- 事务5
begin;
-- 更新数据的时候，旧数据的删除版本被记录为当前事务 ID 5（undo），产生了一条新数据，创建 ID 为当前事务 ID 5：
update user_info set Fname ='一盆鱼' where fpkid=1;
commit;

-- 上面通过版本号的控制，无论其他事务是插入、修改、删除，第一个事务查询到的数据都没有变化。
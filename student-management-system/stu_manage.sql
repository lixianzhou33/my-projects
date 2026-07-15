Create Database stu_manage Character Set UTF8;
use stu_manage;
create table tb_admin (
    username varchar(20),
    password varchar(20),
    PRIMARY KEY (`username`)
);
insert into tb_admin values('admin','admin');
create table tb_clazz (
    clazzno varchar(20), -- 班级编号
    name varchar(20), -- 班级名
    PRIMARY KEY (`clazzno`)
);
insert into tb_clazz values('1001','软件一班');
insert into tb_clazz values('1002','软件二班');

create table tb_student (
    sno varchar(20), -- 学号
    password varchar(20), -- 密码
    name varchar(20), -- 姓名
    tele char(11), -- 电话
    enterdate date, -- 入学时间
    age int, -- 年龄
    gender char(1), -- m 男 w 女
    address varchar(100), -- 详细地址
    clazzno varchar(100), -- 班级
    PRIMARY KEY (`sno`)
);
insert into tb_student values('2023001','123','张三','15555555555','2023-09-01',19,'m','广东深圳xxx','1001');
insert into tb_student values('2023002','123','李四','15555555555','2023-09-01',19,'m','北京xxx','1001');
insert into tb_student values('2023003','123','赵五','15666555555','2023-09-01',19,'w','福建福州xxx','1002');
alter table tb_student add CONSTRAINT frn_stu_clazz
 FOREIGN KEY(clazzno) REFERENCES tb_clazz (clazzno);


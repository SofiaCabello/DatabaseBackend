create table drug
(
    ID           int auto_increment
        primary key,
    name         varchar(255) not null,
    type         varchar(50)  not null,
    manufacturer varchar(255) not null,
    description  varchar(255) not null,
    stock        int          null,
    price_out    double       not null,
    price_in     double       null
);

create index price_index
    on drug (price_out);

create index stock_index
    on drug (stock);

create table finished
(
    id         int          not null
        primary key,
    custom     varchar(255) null,
    order_type varchar(50)  null,
    time       varchar(100) null,
    profit     double       null
);

create table drugFinished
(
    order_ID int    not null,
    drug_ID  int    not null,
    quantity int    not null,
    profit   double null,
    primary key (order_ID, drug_ID),
    constraint drugFinished_fk
        foreign key (drug_ID) references drug (ID)
            on update cascade on delete cascade,
    constraint drugFinished_fk2
        foreign key (order_ID) references finished (id)
            on update cascade on delete cascade
);

create table unfinished
(
    id         int auto_increment
        primary key,
    custom     varchar(255) null,
    order_type varchar(50)  null,
    time       varchar(100) null
);

create table drugUnfinished
(
    order_ID int not null,
    drug_ID  int not null,
    quantity int not null,
    primary key (order_ID, drug_ID),
    constraint drugUnfinished_fk
        foreign key (drug_ID) references drug (ID)
            on update cascade on delete cascade,
    constraint drugUnfinished_fk2
        foreign key (order_ID) references unfinished (id)
            on update cascade on delete cascade
);

create definer = root@localhost trigger after_update_unfinished
    after update
    on unfinished
    for each row
BEGIN
    IF NEW.time IS NOT NULL AND OLD.time IS NULL THEN
        CALL MoveRecordToFinished(NEW.id);
    END IF;
end;

create table user
(
    id       int auto_increment
        primary key,
    username varchar(50) not null,
    password varchar(50) not null,
    role     varchar(50) not null,
    constraint username_unique
        unique (username)
);

create definer = root@localhost view finishedorders as
select `f`.`id`         AS `ID`,
       `d`.`name`       AS `name`,
       `f`.`custom`     AS `custom`,
       `f`.`order_type` AS `order_type`,
       `f`.`time`       AS `time`,
       `f`.`profit`     AS `profit`
from ((`mydb`.`finished` `f` join `mydb`.`drug` `d`) join `mydb`.`drugfinished` `df`)
where ((`f`.`id` = `df`.`order_ID`) and (`d`.`ID` = `df`.`drug_ID`));

create definer = root@localhost view unfinishedorders as
select `uf`.`id`         AS `ID`,
       `d`.`name`        AS `name`,
       `uf`.`custom`     AS `custom`,
       `uf`.`order_type` AS `order_type`,
       `uf`.`time`       AS `time`
from `mydb`.`unfinished` `uf`
         join `mydb`.`drug` `d`
         join `mydb`.`drugunfinished` `du`
where ((`uf`.`id` = `du`.`order_ID`) and (`d`.`ID` = `du`.`drug_ID`));

create
    definer = root@localhost procedure MoveRecordToFinished(IN v_id int)
BEGIN
    INSERT INTO finished(id, custom, order_type, time)
    SELECT v_id, custom, order_type, time
    from unfinished
    where unfinished.id = v_id;

    INSERT INTO drugFinished(order_ID, drug_ID, quantity)
    SELECT order_ID, drug_ID, quantity
    from drugUnfinished
    where drugUnfinished.order_ID = v_id;

    -- 一个订单可能对应多种药物，同时更新其库存
    IF (SELECT order_type FROM finished WHERE id = v_id) = '进货' THEN
        UPDATE drug
        JOIN drugFinished dF on drug.ID = dF.drug_ID
        SET drug.stock = drug.stock + dF.quantity
        WHERE dF.order_ID = v_id;
    ELSEIF (SELECT order_type FROM finished WHERE id = v_id) = '销售' THEN
        UPDATE drug
        JOIN drugFinished dF on drug.ID = dF.drug_ID
        SET drug.stock = drug.stock - dF.quantity
        WHERE dF.order_ID = v_id;
    end if;
    

    -- 计算单种药物利润
    IF (SELECT order_type FROM finished WHERE id = v_id) = '进货' THEN
        UPDATE drug
        JOIN drugFinished dF on drug.ID = dF.drug_ID
        SET dF.profit = dF.quantity * ( 0 - drug.price_in)
        WHERE dF.order_ID = v_id;
    ELSEIF (SELECT order_type FROM finished WHERE id = v_id) = '销售' THEN
        UPDATE drug
        JOIN drugFinished dF on drug.ID = dF.drug_ID
        SET dF.profit = dF.quantity * (drug.price_out - drug.price_in)
        WHERE dF.order_ID = v_id;
    end if;

    
    -- 计算订单利润，用聚合函数GROUP BY
    UPDATE finished
    SET profit = (SELECT SUM(profit) FROM drugFinished GROUP BY order_ID HAVING order_ID = v_id)
    WHERE id = v_id;


    DELETE FROM drugUnfinished WHERE drugUnfinished.order_ID = v_id;
    DELETE FROM unfinished WHERE unfinished.id = v_id;
end;



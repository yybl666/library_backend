
CREATE DATABASE IF NOT EXISTS library_system
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE library_system;


-- 1. 用户表
CREATE TABLE `user` (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '用户姓名',
    password VARCHAR(255) NOT NULL COMMENT '密码哈希值',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    role VARCHAR(20) NOT NULL DEFAULT 'READER' COMMENT '角色：READER借书者，ADMIN图书管理员',

    CONSTRAINT uk_user_name UNIQUE (name),
    CONSTRAINT uk_user_email UNIQUE (email),

    CONSTRAINT chk_user_role
        CHECK (role IN ('READER', 'ADMIN'))
) ENGINE=InnoDB COMMENT='用户表';


-- 2. 图书表：记录书目信息
CREATE TABLE book (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    isbn VARCHAR(20) NOT NULL COMMENT 'ISBN',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    title VARCHAR(200) NOT NULL COMMENT '书名',

    CONSTRAINT uk_book_isbn UNIQUE (isbn)
) ENGINE=InnoDB COMMENT='图书书目表';


-- 3. 图书馆藏表：记录每一本实体书
CREATE TABLE book_copy (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '馆藏ID',
    book_id BIGINT UNSIGNED NOT NULL COMMENT '所属书目ID',
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
        COMMENT '馆藏状态：AVAILABLE在馆，BORROWED借出',

    CONSTRAINT fk_copy_book
        FOREIGN KEY (book_id) REFERENCES book(id),

    CONSTRAINT chk_copy_status
        CHECK (status IN ('AVAILABLE', 'BORROWED')),

    INDEX idx_copy_book_id (book_id)
) ENGINE=InnoDB COMMENT='图书馆藏表';


-- 4. 借阅记录表：记录每一次借书和还书
CREATE TABLE borrow_record (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '借阅记录ID',
    book_copy_id BIGINT UNSIGNED NOT NULL COMMENT '馆藏ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '借书者ID',

    status VARCHAR(20) NOT NULL DEFAULT 'BORROWED'
        COMMENT '借阅状态：BORROWED借阅中，RETURNED已归还',

    borrow_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '借出时间',

    return_time DATETIME DEFAULT NULL
        COMMENT '实际归还时间，未归还时为空',

    due_time DATETIME NOT NULL
        COMMENT '应还时间',

    CONSTRAINT fk_record_copy
        FOREIGN KEY (book_copy_id) REFERENCES book_copy(id),

    CONSTRAINT fk_record_user
        FOREIGN KEY (user_id) REFERENCES `user`(id),

    CONSTRAINT chk_record_status
        CHECK (status IN ('BORROWED', 'RETURNED')),

    INDEX idx_record_user_status (user_id, status),
    INDEX idx_record_copy_status (book_copy_id, status),
    INDEX idx_record_due_time (due_time)
) ENGINE=InnoDB COMMENT='借阅记录表';

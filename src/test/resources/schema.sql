CREATE TABLE students
(
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID (主キー)
    name VARCHAR(100) NOT NULL,                      -- 名前
    ruby VARCHAR(100) NOT NULL,                      -- ルビ（振り仮名）
    nickname VARCHAR(100),                           -- ニックネーム (NULL 許可)
    email VARCHAR(100) NOT NULL,                     -- メールアドレス
    address VARCHAR(100),                            -- 住所 (NULL 許可)
    age INT,                                         -- 年齢 (NULL 許可)
    gender VARCHAR(100),                             -- 性別 (NULL 許可)
    remark VARCHAR(255),                             -- 備考 (NULL 許可)
    is_deleted boolean DEFAULT FALSE                 -- 削除フラグ (デフォルト 0)
);

CREATE TABLE students_courses
 (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID (主キー)
    student_id INT NOT NULL,                         -- 学生ID (外部キー)
    course_name VARCHAR(100) NOT NULL,               -- コース名
    start_date TIMESTAMP,                            -- 開始日時 (NULL 許可)
    end_date TIMESTAMP,                              -- 終了日時 (NULL 許可)
    FOREIGN KEY (student_id) REFERENCES students(id) -- 外部キー制約 (studentsテーブルのid)
);

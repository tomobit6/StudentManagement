# 受講生管理システムについて
画像
受講生を管理するCRUD機能を持つアプリです。
## アプリケーションのサービス概要
- 昨今オンライン塾やオンライン英会話など、場所に関係なく受講できるサービスが増えているため、それらの受講生管理すべてに対応できる汎用的な受講生管理システムです。
- 主な機能は以下の通り
  - **受講生情報の一覧表示：** 現在利用登録している受講生の一覧を表示します。
  - **受講生情報の詳細検索：** 登録されている受講生idに基づいて、受講生の情報を検索・閲覧します。
  - **受講生情報の登録：** 新たに加入した受講生の登録を行います。
  - **受講生情報の更新：** 登録されている受講生idに基づいて、受講生の情報を更新します。
  - **受講生情報の削除：** 登録されている受講生idに基づいて、受講生の情報を削除します。（論理削除）
## 利用方法

## 開発環境一覧

## 設計書
### API仕様書
### E-R図
```mermaid
erDiagram
  users {
    int id "ID"
    string name "名前"
    string ruby "ふりがな"
    string nickname "ニックネーム"
    string email "メールアドレス"
    string address "住所"
    int age "年齢"
    string gender "性別" "男、女、その他のいずれか"
    string remark "備考"
    boolean is_Deleted "削除フラグ"
  }
```
### シーケンス図
登録はまだしも更新については今のところvoidなので返りがない。ただ、HTTPレスポンスは返すため、これだけを返す表記をしたらいいのか？
```mermaid
sequenceDiagram
    actor User
    participant BR as Browser
    participant DB as Database

　　%% 受講生情報（全件）の取得フロー
　　Note right of User: 受講生情報（全件）の取得フロー
    User->>BR: GET /studetnts（受講生の一覧検索）
    BR->>DB: SELECT受講生全件
    DB-->>BR: 受講生一覧（全件）
    BR-->>User: 200 OK (受講生全件の詳細情報が返る）


　　%% 受講生情報の取得フロー
    Note right of User: 受講生情報の取得フロー
    User->>BR: GET /studetnts/{id}（IDに紐づく受講生の検索）

　　alt 受講生IDの形式が正しい場合
      BR->>DB: SELECT受講生情報
      alt 受講生IDが存在する場合
        DB-->>BR: IDに紐づく受講生情報
        BR-->>User: 200 OK (受講生の詳細情報が返る）
　　  else 受講生IDが存在しない場合
        DB-->>BR: null
        BR-->>User: 404 Not Found
　　　end
   else 受講生IDの形式が不正な場合
　　　BR-->User: 400 Bad Request
   end


   %% 受講生情報の登録フロー
   Note right of User: 受講生情報の登録フロー
   User->>BR: POST /registerStudent（受講生の登録）
   alt 受講生情報の入力データが有効な場合
     BR->>DB: INSERT受講生情報
     DB-->>BR: 登録された受講生情報
     BR-->>User: 200 OK (登録された受講生情報が返る）
   else 受講生情報の入力データが不正な場合
     BR-->>User:400 Bad Request
   end

   %% 受講生情報の更新（論理削除含む）フロー
   Note right of User: 受講生情報の更新（論理削除含む）フロー
   User->>BR: PUT /updateStudent（受講生の更新）
   alt 受講生情報の入力データが有効な場合
     BR->>DB: update受講生情報
     DB-->>BR: 更新（削除）された受講生情報
     BR-->>User: 200 OK (更新（削除）された受講生情報が返る）
   else 受講生情報の入力データが不正な場合
     BR-->>User:400 Bad Request
   end


### インフラ構成図
### URL一覧

## テスト一覧

## 工夫した点と課題

## 今後の展望

## 終わりに



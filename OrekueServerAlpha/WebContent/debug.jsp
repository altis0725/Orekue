<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>ユーザ登録</h2>
<form method="get" action="CreateUser">
<div>device_id:<input type="text" name="device_id" value="default"/></div>
<div>account_name:<input type="text" name="account_name" /></div>
<div>hashed_password:<input type="text" name="hashed_password" value="default"/></div>
<div>name:<input type="text" name="name" value="user"/></div>
<input type="submit" value="submit" />
</form>

<h2>ユーザ情報取得</h2>
<form method="get" action="GetUser">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>アクティビテ投稿</h2>
<form method="get" action="PostActivity">
<div>user_id:<input type="text" name="user_id" /></div>
<div>tag_id:<input type="text" name="tag_id" value="0" /></div>
<div>category_id:<input type="text" name="category_id" value="0" /></div>
<div>duration:<input type="text" name="duration" value="1" /></div>
<div>date:<input type="text" name="date" value="0" /></div>
<div>content:<textarea name="content"></textarea></div>
<input type="submit" value="submit" />
</form>

<h2>活動履歴</h2>
<form method="get" action="GetActivityList">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>友達ID取得</h2>
<form method="get" action="GetFriendsId">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>友達情報取得</h2>
<form method="get" action="GetFriendsList">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>タイムライン取得</h2>
<form method="get" action="GetTimeLine">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>友達作成</h2>
<form method="get" action="MakeFriend">
<div>user_id_1:<input type="text" name="user_id_1" /></div>
<div>user_id_2:<input type="text" name="user_id_2" /></div>
<input type="submit" value="submit" />
</form>

<h2>友達一覧(ID)</h2>
<form method="get" action="GetFriendsId">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>友達一覧(User List)</h2>
<form method="get" action="GetFriendsList">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>ランキング(User List)</h2>
<form method="get" action="GetRanking">
<div>user_id:<input type="text" name="user_id" /></div>
<div>category_id:<input type="text" name="category_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>称号マスターデータ</h2>
<form method="get" action="GetTitle">
<div>title_id:<input type="text" name="title_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>接頭辞マスターデータ</h2>
<form method="get" action="GetPrefix">
<div>prefix_id:<input type="text" name="prefix_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>解放済み称号</h2>
<form method="get" action="GetReleasedTitle">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>解放済み接頭辞</h2>
<form method="get" action="GetReleasedPrefix">
<div>user_id:<input type="text" name="user_id" /></div>
<input type="submit" value="submit" />
</form>


<h2>称号解放</h2>
<form method="get" action="ReleaseTitle">
<div>user_id:<input type="text" name="user_id" /></div>
<div>title_id:<input type="text" name="title_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>接頭辞解放</h2>
<form method="get" action="ReleasePrefix">
<div>user_id:<input type="text" name="user_id" /></div>
<div>prefix_id:<input type="text" name="prefix_id" /></div>
<input type="submit" value="submit" />
</form>

<h2>タグ取得</h2>
<form method="get" action="GetTags">
<div>category_id:<input type="text" name="category_id" /></div>
<input type="submit" value="submit" />
</form>

</body>
</html>
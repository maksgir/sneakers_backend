drop table if exists ticket;

drop table if exists sellers;

drop table if exists reviews;

drop table if exists sneaker_ratings;

drop table if exists sneaker_images;

drop table if exists sneaker_categories;

drop table if exists wishlists;

drop table if exists sneakers;

drop table if exists messages;

drop table if exists users;

drop function if exists update_seller_rating(integer, integer);

drop function if exists update_seller_rating_on_review();

drop function if exists check_reviewer_not_equal_reviewed();

drop function if exists check_seller_not_equal_buyer();


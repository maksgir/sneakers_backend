-- user table ----------------------------------

CREATE TABLE users (
                       user_id VARCHAR PRIMARY KEY,
                       first_name VARCHAR,
                       last_name VARCHAR,
                       username VARCHAR UNIQUE NOT NULL,
                       password VARCHAR NOT NULL,
                       email VARCHAR UNIQUE NOT NULL,
                       phone_number VARCHAR NOT NULL
);

-- seller table ----------------------------------

CREATE TABLE sellers (
                         seller_id VARCHAR PRIMARY KEY,
                         user_id VARCHAR UNIQUE NOT NULL,
                         rating INTEGER NOT NULL
);

ALTER TABLE sellers
    ADD CONSTRAINT fk_sellers_users
        FOREIGN KEY (user_id) REFERENCES users(user_id);


-- sneakers table ----------------------------------

CREATE TABLE sneakers (
                          sneaker_id VARCHAR PRIMARY KEY,
                          model VARCHAR NOT NULL,
                          brand VARCHAR NOT NULL,
                          size DECIMAL NOT NULL,
                          color VARCHAR,
                          condition VARCHAR NOT NULL,
                          price BIGINT NOT NULL
);

-- ticket table ----------------------------------

CREATE TABLE ticket (
                        transaction_id VARCHAR PRIMARY KEY,
                        buyer_id VARCHAR NOT NULL,
                        seller_id VARCHAR NOT NULL,
                        sneaker_id VARCHAR NOT NULL,
                        date_time TIMESTAMP NOT NULL,
                        amount bigint NOT NULL,
                        status VARCHAR NOT NULL,
                        descriprion VARCHAR
);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_buyers
        FOREIGN KEY (buyer_id) REFERENCES users(user_id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_sellers
        FOREIGN KEY (seller_id) REFERENCES sellers(user_id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_sneakers
        FOREIGN KEY (sneaker_id) REFERENCES sneakers(sneaker_id);

-- reviews table ----------------------------------

CREATE TABLE reviews (
                         review_id VARCHAR PRIMARY KEY,
                         text VARCHAR,
                         rating DECIMAL NOT NULL,
                         date_posted TIMESTAMP NOT NULL,
                         reviewer_id varchar NOT NULL,
                         reviewed_user_id varchar NOT NULL
);

ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_reviewers
        FOREIGN KEY (reviewer_id) REFERENCES users(user_id);

ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_reviewed_users
        FOREIGN KEY (reviewed_user_id) REFERENCES users(user_id);


-- sneaker_ratings table ----------------------------------

CREATE TABLE sneaker_ratings (
                                 sneaker_rating_id VARCHAR PRIMARY KEY,
                                 sneaker_id VARCHAR NOT NULL,
                                 rating DECIMAL NOT NULL
);

ALTER TABLE sneaker_ratings
    ADD CONSTRAINT fk_sneaker_ratings_sneakers
        FOREIGN KEY (sneaker_id) REFERENCES sneakers(sneaker_id);


-- sneaker_images table ----------------------------------

CREATE TABLE sneaker_images (
                                image_id VARCHAR PRIMARY KEY,
                                sneaker_id VARCHAR NOT NULL,
                                image_url VARCHAR NOT NULL
);

ALTER TABLE sneaker_images
    ADD CONSTRAINT fk_sneaker_images_sneakers
        FOREIGN KEY (sneaker_id) REFERENCES sneakers(sneaker_id);


-- sneaker_categories table ----------------------------------

CREATE TABLE sneaker_categories (
                                    category_id VARCHAR PRIMARY KEY,
                                    sneaker_id VARCHAR NOT NULL,
                                    category VARCHAR NOT NULL
);

ALTER TABLE sneaker_categories
    ADD CONSTRAINT fk_sneaker_categories_sneakers
        FOREIGN KEY (sneaker_id) REFERENCES sneakers(sneaker_id);


-- wishlists table ---------------------------------------

CREATE TABLE wishlists (
                           wishlist_id INT PRIMARY KEY,
                           user_id varchar NOT NULL,
                           sneaker_id varchar NOT NULL
);

ALTER TABLE wishlists
    ADD CONSTRAINT fk_wishlists_users
        FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE wishlists
    ADD CONSTRAINT fk_wishlists_sneakers
        FOREIGN KEY (sneaker_id) REFERENCES sneakers(sneaker_id);


-- messages table ----------------------------------

CREATE TABLE messages (
                          message_id varchar PRIMARY KEY,
                          sender_id varchar NOT NULL,
                          receiver_id varchar NOT NULL,
                          text VARCHAR NOT NULL,
                          date_time TIMESTAMP
);

ALTER TABLE messages
    ADD CONSTRAINT fk_messages_senders
        FOREIGN KEY (sender_id) REFERENCES users(user_id);

ALTER TABLE messages
    ADD CONSTRAINT fk_messages_receivers
        FOREIGN KEY (receiver_id) REFERENCES users(user_id);


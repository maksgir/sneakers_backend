CREATE OR REPLACE FUNCTION update_seller_rating(new_rating INT, seller_id INT)
    RETURNS DECIMAL AS $$
DECLARE
total_rating DECIMAL;
    num_ratings INT;
BEGIN
    SELECT COALESCE(SUM(rating), 0), COALESCE(COUNT(*), 0)
    INTO total_rating, num_ratings
    FROM reviews
    WHERE reviewed_user_id = seller_id;

    total_rating := total_rating + new_rating;
        num_ratings := num_ratings + 1;

    RETURN total_rating / num_ratings;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_seller_rating_on_review()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE sellers
    SET rating = update_seller_rating(NEW.rating, NEW.reviewed_user_id)
    WHERE user_id = NEW.reviewed_user_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_reviewer_not_equal_reviewed()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.reviewer_id = NEW.reviewed_user_id THEN
        RAISE EXCEPTION 'Reviewer cannot be the same as the reviewed user.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_seller_not_equal_buyer()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.seller_id = NEW.buyer_id THEN
        RAISE EXCEPTION 'Seller cannot be the same as the buyer.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

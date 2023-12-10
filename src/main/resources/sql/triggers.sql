-- checks

ALTER TABLE reviews
    ADD CONSTRAINT chk_reviews_rating_range
        CHECK (rating >= 0 AND rating <= 5);

ALTER TABLE sneaker_ratings
    ADD CONSTRAINT chk_sneaker_ratings_rating_range
        CHECK (rating >= 0 AND rating <= 5);


-- triggers

CREATE TRIGGER check_reviewer_not_equal_reviewed
    BEFORE INSERT ON reviews
    FOR EACH ROW
    EXECUTE FUNCTION check_reviewer_not_equal_reviewed();

CREATE TRIGGER check_seller_not_equal_buyer
    BEFORE INSERT ON ticket
    FOR EACH ROW
    EXECUTE FUNCTION check_seller_not_equal_buyer();

CREATE TRIGGER update_seller_rating_on_review
    AFTER INSERT ON reviews
    FOR EACH ROW
    EXECUTE FUNCTION update_seller_rating_on_review();

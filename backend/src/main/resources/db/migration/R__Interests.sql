CREATE TABLE Interest (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    area VARCHAR(50) NOT NULL
);

CREATE TABLE Interests (
    int_id BIGSERIAL NOT NULL,
    person_id BIGSERIAL NOT NULL,
    FOREIGN KEY (int_id) REFERENCES Interest(id),
    FOREIGN KEY (person_id) REFERENCES Person(id)
);
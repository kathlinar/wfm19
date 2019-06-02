-- persons
INSERT INTO Person (id, birthday, first_name, last_name, type)
VALUES (1, '1990-01-01', 'Max', 'Musterguide 1', 'GUIDE'),
       (2, '1990-02-01', 'Max', 'Musterguide 2', 'GUIDE'),
       (3, '1990-03-01', 'Franz', 'Mustermann 1', 'CUSTOMER'),
       (4, '1990-04-01', 'Franz', 'Mustermann 2', 'CUSTOMER'),
       (5, '1990-05-01', 'Franz', 'Mustermann 3', 'CUSTOMER');

INSERT INTO Person (id,email,first_name,last_name,password,type)
VALUES(6,'k.riginding@gmail.com','Kathleen','Riginding','1234wfm','GUIDE'),
      (7,'e1427322@student.tuwien.ac.at','Christoph','Huber','test','GUIDE');

-- addresses
INSERT INTO Address (id, city, street, zip)
VALUES (1, 'Vienna', 'Am Ring 1', '1010'),
       (2, 'Vienna', 'Am Ring 2', '1010'),
       (3, 'Vienna', 'Am Ring 3', '1010'),
       (4, 'Melk', 'Stift 1', '3390'),
       (5, 'Vienna', 'Schloss Schönbrunn', '1130');

-- experiences
INSERT INTO Experience (id, description, duration, max_group_size, name, price, type, location_id)
VALUES (1, 'Tagesausflug von Wien zum Stift Melk und ins Donautal', '06:00', 30, 'Tagesausflug zum Stift Melk', 80,
        'SIGHTSEEING', 4),
       (2,
        'Lauschen Sie der Musik von Mozart und Strauss in der herrlichen Atmosphäre der Orangerie im Schloss Schönbrunn.',
        '01:30', 300, 'Abendkonzert im Schloss Schönbrunn', 45, 'CONCERT', 5);

-- reservations
INSERT INTO Reservation (experience_id, person_id, reservation_date, attended, deleted, feedback)
VALUES (1, 3, '2019-05-01', true, false, 'was a great day with my lads'),
       (2, 4, '2019-06-10', null, false, null),
       (2, 5, '2019-06-15', null, false, null);

-- person_experience
INSERT INTO person_experience(person_id, experience_id)
values (1, 1),
       (2, 1),
       (1, 2);
-- persons
INSERT INTO Person (id, birthday, first_name, last_name, type)
VALUES (1, '1990-01-01', 'Max', 'Musterguide 1', 'GUIDE'),
       (2, '1990-02-01', 'Max', 'Musterguide 2', 'GUIDE'),
       (3, '1990-03-01', 'Franz', 'Mustermann 1', 'CUSTOMER'),
       (4, '1990-04-01', 'Franz', 'Mustermann 2', 'CUSTOMER'),
       (5, '1990-05-01', 'Franz', 'Mustermann 3', 'CUSTOMER');

INSERT INTO Address (id, city, street, zip)
VALUES (1, 'Vienna', 'Am Ring 1', '1010'),
       (2, 'Vienna', 'Am Ring 2', '1010'),
       (3, 'Vienna', 'Am Ring 3', '1010'),
       (4, 'Melk', 'Stift 1', '3390'),
       (5, 'Vienna', 'Schloss Schönbrunn', '1130'),
       (6,'Vienna','Prater Hauptallee','1020'),
       (7,'Vienna', 'Donaukanal', '1010'),
       (8,'Vienna','Linke Wienzeile 22', '1060'),
       (9,'Vienna', 'Lindengasse 53','1070'),
       (10,'Vienna','Hernalser Guertel 20','1080'),
       (11,'Vienna','Wipplingerstraße 9','1010');

-- addresses
INSERT INTO Person (id,email,first_name,last_name,password,type)
VALUES(6,'k.riginding@gmail.com','Kathleen','Riginding','$31$16$vy_nqNAQuuvTxPNX0nxUXon5UWC_Z4_-VXOAn5UnIlo','GUIDE'),
      (7,'e1427322@student.tuwien.ac.at','Christoph','Huber','$31$16$fvCaqiDruLg3LMwv09V6KTg-M5SIZTvCbQI3yBw94E8','GUIDE'),
      (8,'e1426163@student.tuwien.ac.at','Samuel','Alkin','$31$16$fvCaqiDruLg3LMwv09V6KTg-M5SIZTvCbQI3yBw94E8','GUIDE'),
      (9,'samuel.alkin@gmx.at','Samuel','Alkin','$31$16$fvCaqiDruLg3LMwv09V6KTg-M5SIZTvCbQI3yBw94E8','CUSTOMER'),
      (10,'e1427619@student.tuwien.ac.at','Patrick','Fichtinger','$31$16$fvCaqiDruLg3LMwv09V6KTg-M5SIZTvCbQI3yBw94E8','GUIDE');


-- experiences
INSERT INTO Experience (id, description, duration, max_group_size, name, price, type, location_id)
VALUES (1, 'Tagesausflug von Wien zum Stift Melk und ins Donautal', '06:00', 30, 'Tagesausflug zum Stift Melk', 80,
        'SIGHTSEEING', 4),
       (2,
        'Lauschen Sie der Musik von Mozart und Strauss in der herrlichen Atmosphäre der Orangerie im Schloss Schönbrunn.',
        '01:30', 300, 'Abendkonzert im Schloss Schönbrunn', 45, 'CONCERT', 5),
        (3,'We will run through the Allee in a relaxed pace. Beginners welcome!','01:30',10,'Morning Run - Prater', 0,
        'SPORTS',6),
        (4,'Afternoon 10k run along the water and drinks','01:30',10,'Afternoon Run - Donaukanal',10,'SPORTS',7),
        (5,'We will visit modern coffeehouses to drink some coffee and people watch','02:00',5,
        'Modern coffee house tour',20,'SPECIAL',8),
        (6, 'Playing VR games + drinks afterwards','03:00',15,'VR Games at VREI',25,'GAMES',9),
        (7, 'We will split in two groups and play against each other','01:30',16,'Escape the Room',20,'GAMES',10),
        (8,'Beer tasting, snacks and more','01:30',10,'Austrian Beer Tasting Experience',15,'SPECIAL',11);

-- reservations
INSERT INTO Reservation (reservation_id,experience_id, person_id, reservation_date, attended, deleted, feedback)
VALUES (1,1, 3, '2019-05-01', true, false, 'was a great day with my lads'),
       (2,2, 4, '2019-06-10', null, false, null),
       (3,2, 5, '2019-06-15', null, false, null);

-- person_experience
INSERT INTO person_experience(person_id, experience_id)
values (1, 1),
       (2, 1),
       (1, 2);
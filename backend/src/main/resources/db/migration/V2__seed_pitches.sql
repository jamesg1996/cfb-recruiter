INSERT INTO pitch (name) VALUES ('College Experience');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'College Experience'), 'ACADEMIC_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'College Experience'), 'CAMPUS_LIFESTYLE'),
    ((SELECT id FROM pitch WHERE name = 'College Experience'), 'STADIUM_ATMOSPHERE');

INSERT INTO pitch (name) VALUES ('Team Player');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Team Player'), 'COACH_STABILITY'),
    ((SELECT id FROM pitch WHERE name = 'Team Player'), 'PLAYING_STYLE'),
    ((SELECT id FROM pitch WHERE name = 'Team Player'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Campus Personality');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Campus Personality'), 'CAMPUS_LIFESTYLE'),
    ((SELECT id FROM pitch WHERE name = 'Campus Personality'), 'PLAYING_STYLE'),
    ((SELECT id FROM pitch WHERE name = 'Campus Personality'), 'PLAYING_TIME');

INSERT INTO pitch (name) VALUES ('Gamer');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Gamer'), 'CONFERENCE_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Gamer'), 'PLAYING_STYLE'),
    ((SELECT id FROM pitch WHERE name = 'Gamer'), 'PRO_POTENTIAL');

INSERT INTO pitch (name) VALUES ('Standard Bearer');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Standard Bearer'), 'COACH_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Standard Bearer'), 'CONFERENCE_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Standard Bearer'), 'PLAYING_STYLE');

INSERT INTO pitch (name) VALUES ('Student Of The Game');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Student Of The Game'), 'ACADEMIC_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Student Of The Game'), 'COACH_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Student Of The Game'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Hometown Hero');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Hometown Hero'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'Hometown Hero'), 'PROGRAM_TRADITION'),
    ((SELECT id FROM pitch WHERE name = 'Hometown Hero'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Status Seeker');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Status Seeker'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'Status Seeker'), 'COACH_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Status Seeker'), 'CONFERENCE_PRESTIGE');

INSERT INTO pitch (name) VALUES ('The Clutch');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'The Clutch'), 'COACH_STABILITY'),
    ((SELECT id FROM pitch WHERE name = 'The Clutch'), 'PLAYING_STYLE'),
    ((SELECT id FROM pitch WHERE name = 'The Clutch'), 'PLAYING_TIME');

INSERT INTO pitch (name) VALUES ('Primetime Player');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Primetime Player'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'Primetime Player'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'Primetime Player'), 'PLAYING_TIME');

INSERT INTO pitch (name) VALUES ('Coach Connection');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Coach Connection'), 'ATHLETIC_FACILITIES'),
    ((SELECT id FROM pitch WHERE name = 'Coach Connection'), 'COACH_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Coach Connection'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Aspirational Goals');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Aspirational Goals'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'Aspirational Goals'), 'COACH_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Aspirational Goals'), 'CONFERENCE_PRESTIGE');

INSERT INTO pitch (name) VALUES ('House Call');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'House Call'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'House Call'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'House Call'), 'COACH_PRESTIGE');

INSERT INTO pitch (name) VALUES ('Football Influencer');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Football Influencer'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'Football Influencer'), 'PLAYING_TIME'),
    ((SELECT id FROM pitch WHERE name = 'Football Influencer'), 'PRO_POTENTIAL');

INSERT INTO pitch (name) VALUES ('Clocked In');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Clocked In'), 'PLAYING_STYLE'),
    ((SELECT id FROM pitch WHERE name = 'Clocked In'), 'PLAYING_TIME'),
    ((SELECT id FROM pitch WHERE name = 'Clocked In'), 'PRO_POTENTIAL');

INSERT INTO pitch (name) VALUES ('Star Search');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Star Search'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'Star Search'), 'PLAYING_TIME'),
    ((SELECT id FROM pitch WHERE name = 'Star Search'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Grassroots Traditionalist');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Grassroots Traditionalist'), 'PROGRAM_TRADITION'),
    ((SELECT id FROM pitch WHERE name = 'Grassroots Traditionalist'), 'PROXIMITY_TO_HOME'),
    ((SELECT id FROM pitch WHERE name = 'Grassroots Traditionalist'), 'STADIUM_ATMOSPHERE');

INSERT INTO pitch (name) VALUES ('Conference Legend');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Conference Legend'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'Conference Legend'), 'CONFERENCE_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Conference Legend'), 'PROXIMITY_TO_HOME');

INSERT INTO pitch (name) VALUES ('Sunday Player');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Sunday Player'), 'CHAMPIONSHIP_CONTENDER'),
    ((SELECT id FROM pitch WHERE name = 'Sunday Player'), 'CONFERENCE_PRESTIGE'),
    ((SELECT id FROM pitch WHERE name = 'Sunday Player'), 'PRO_POTENTIAL');

INSERT INTO pitch (name) VALUES ('Gym Rat');
INSERT INTO pitch_motivation (pitch_id, category) VALUES
    ((SELECT id FROM pitch WHERE name = 'Gym Rat'), 'ATHLETIC_FACILITIES'),
    ((SELECT id FROM pitch WHERE name = 'Gym Rat'), 'BRAND_EXPOSURE'),
    ((SELECT id FROM pitch WHERE name = 'Gym Rat'), 'PRO_POTENTIAL');
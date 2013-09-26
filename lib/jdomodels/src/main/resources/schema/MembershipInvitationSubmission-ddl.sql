CREATE TABLE `MEMBERSHIP_INVITATION_SUBMISSION` (
  `MIS_ID` bigint(20) NOT NULL,
  `MIS_ETAG` char(36) NOT NULL,
  `TEAM_ID` bigint(20) NOT NULL,
  `EXPIRES_ON` bigint(20),
  `MIS_PROPERTIES` mediumblob,
  PRIMARY KEY (`MIS_ID`),
  CONSTRAINT `MEMBERSHIP_INVITATION_TEAM_FK` FOREIGN KEY (`TEAM_ID`) REFERENCES `TEAM` (`ID`)
)
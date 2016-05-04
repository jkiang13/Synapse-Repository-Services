CREATE TABLE IF NOT EXISTS `DISCUSSION_THREAD_VIEW` (
  `ID` bigint(20) NOT NULL,
  `THREAD_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`THREAD_ID`, `USER_ID`),
  UNIQUE KEY `DISCUSSION_THREAD_VIEW_BACK_UP_ID`(`ID`),
  INDEX `DISCUSSION_THREAD_VIEW_USER_ID_INDEX` (`USER_ID`),
  CONSTRAINT `DISCUSSION_THREAD_VIEW_THREAD_ID_FK` FOREIGN KEY (`THREAD_ID`) REFERENCES `DISCUSSION_THREAD` (`ID`) ON DELETE CASCADE
)

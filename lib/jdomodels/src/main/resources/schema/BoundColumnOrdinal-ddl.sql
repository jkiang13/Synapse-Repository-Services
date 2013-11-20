CREATE TABLE `BOUND_COLUMN_ORDINAL` (
  `COLUMN_ID` bigint(20) NOT NULL,
  `OBJECT_ID` bigint(20) NOT NULL,
  `ORDINAL` bigint(20) NOT NULL,
  PRIMARY KEY (`COLUMN_ID`, `OBJECT_ID`),
  CONSTRAINT `ORD_COL_MODEL_FK` FOREIGN KEY (`COLUMN_ID`) REFERENCES `COLUMN_MODEL` (`ID`)
)
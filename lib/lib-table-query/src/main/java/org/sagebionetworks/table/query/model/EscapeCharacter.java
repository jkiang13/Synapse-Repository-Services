package org.sagebionetworks.table.query.model;

/**
 * This matches &ltescape character&gt  in: <a href="http://savage.net.au/SQL/sql-92.bnf">SQL-92</a>
 */
public class EscapeCharacter implements SQLElement {

	CharacterValueExpression characterValueExpression;

	public EscapeCharacter(CharacterValueExpression characterValueExpression) {
		super();
		this.characterValueExpression = characterValueExpression;
	}

	public CharacterValueExpression getCharacterValueExpression() {
		return characterValueExpression;
	}
	
	@Override
	public void toSQL(StringBuilder builder) {
		characterValueExpression.toSQL(builder);
	}
}

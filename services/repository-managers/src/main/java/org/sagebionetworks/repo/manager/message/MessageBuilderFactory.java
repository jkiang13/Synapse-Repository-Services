package org.sagebionetworks.repo.manager.message;

import org.sagebionetworks.repo.model.message.ChangeType;

/**
 * Factory that generates message builders.
 *
 */
public interface MessageBuilderFactory {

	/**
	 * Create a message builder that will be used to build all messages for the given change event.
	 * 
	 * @param objectId
	 * @param changeType
	 * @return
	 */
	BroadcastMessageBuilder createMessageBuilder(String objectId, ChangeType changeType);
}
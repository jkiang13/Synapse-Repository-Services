package org.sagebionetworks.repo.web.controller;

import static org.sagebionetworks.repo.web.UrlHelpers.EVALUATION_ID_PATH_VAR_WITHOUT_BRACKETS;
import static org.sagebionetworks.repo.web.UrlHelpers.ID_PATH_VARIABLE;

import java.io.IOException;

import org.sagebionetworks.reflection.model.PaginatedResults;
import org.sagebionetworks.repo.model.AccessApproval;
import org.sagebionetworks.repo.model.AuthorizationConstants;
import org.sagebionetworks.repo.model.BatchAccessApprovalInfoRequest;
import org.sagebionetworks.repo.model.BatchAccessApprovalInfoResponse;
import org.sagebionetworks.repo.model.DatastoreException;
import org.sagebionetworks.repo.model.InvalidModelException;
import org.sagebionetworks.repo.model.RestrictableObjectDescriptor;
import org.sagebionetworks.repo.model.RestrictableObjectType;
import org.sagebionetworks.repo.model.ServiceConstants;
import org.sagebionetworks.repo.model.UnauthorizedException;
import org.sagebionetworks.repo.model.dataaccess.AccessorGroupRequest;
import org.sagebionetworks.repo.model.dataaccess.AccessorGroupResponse;
import org.sagebionetworks.repo.model.dataaccess.AccessorGroupRevokeRequest;
import org.sagebionetworks.repo.web.NotFoundException;
import org.sagebionetworks.repo.web.UrlHelpers;
import org.sagebionetworks.repo.web.rest.doc.ControllerInfo;
import org.sagebionetworks.repo.web.service.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Access Approval services manage the fulfillment of Access Requirements, on a per-user basis.  
 * (See <a href="#org.sagebionetworks.repo.web.controller.AccessRequirementController">
 * Access Requirement Services</a> for more information.)  Most of the Access Approval services
 * are available only to members of the Synapse Access and Compliance Team (ACT).  
 *
 */
@ControllerInfo(displayName="Access Approval Services", path="repo/v1")
@Controller
@RequestMapping(UrlHelpers.REPO_PATH)
public class AccessApprovalController extends BaseController {
	
	@Autowired
	ServiceProvider serviceProvider;
	
	/**
	 * Create an Access Approval, thereby fulfilling an Access Requirement for a given user.  
	 * Self-signed Access Approvals may be generated by the user being approved.  ACT 
	 * Access Approvals may be generated only by the Synapse Access and Compliance Team (ACT).  
	 * Since an Access Requirement may apply to multiple entities, fulfilling an Access Requirement
	 * provides access to all entities restricted by the fulfilled requirement.
	 * 
	 * @param userId
	 * @param accessApproval the Access Approval to create
	 * @return
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 * @throws InvalidModelException
	 * @throws IOException
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL, method = RequestMethod.POST)
	public @ResponseBody
	AccessApproval createAccessApproval(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@RequestBody AccessApproval accessApproval) throws DatastoreException, UnauthorizedException, NotFoundException, InvalidModelException, IOException {
		return serviceProvider.getAccessApprovalService().createAccessApproval(userId, accessApproval);
	}

	/**
	 * Retrieving an AccessApproval given its ID.
	 * 
	 * @param userId
	 * @param approvalId
	 * @return
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_WITH_APPROVAL_ID, method = RequestMethod.GET)
	public @ResponseBody
	AccessApproval 
	getAccessApproval(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@PathVariable String approvalId
			) throws DatastoreException, UnauthorizedException, NotFoundException {	
		return serviceProvider.getAccessApprovalService().getAccessApproval(userId, approvalId);
	}

	/**
	 * Retrieving a page of AccessorGroup.
	 * This service is only available for ACT.
	 * ACT can filter on AccessRequirementId, submitterId, and expiredOn by setting
	 * the associated fields in AccessorGroupRequest.
	 * 
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_GROUP, method = RequestMethod.POST)
	public @ResponseBody AccessorGroupResponse listAccessorGroup(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@RequestBody AccessorGroupRequest request
			) throws UnauthorizedException, NotFoundException {	
		return serviceProvider.getAccessApprovalService().listAccessorGroup(userId, request);
	}

	/**
	 * Revoke a group of accessors.
	 * Only ACT can perform this action.
	 * 
	 * @param userId
	 * @param request
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_GROUP_REVOKE, method = RequestMethod.PUT)
	public void revokeGroup(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@RequestBody AccessorGroupRevokeRequest request
			) throws UnauthorizedException, NotFoundException {	
		serviceProvider.getAccessApprovalService().revokeGroup(userId, request);
	}

	/**
	 * Retrieve a batch of AccessApprovalInfo for a single user.
	 * 
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_INFO, method = RequestMethod.POST)
	public @ResponseBody BatchAccessApprovalInfoResponse getBatchAccessApprovalInfo(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@RequestBody BatchAccessApprovalInfoRequest request
			) throws UnauthorizedException, NotFoundException {	
		return serviceProvider.getAccessApprovalService().getBatchAccessApprovalInfo(userId, request);
	}

	/**
	 * Retrieve the Access Approvals for the given Entity. This service is only available to the ACT.
	 * 
	 * @param userId
	 * @param entityId the entity of interest
	 * @param limit - Limits the size of the page returned. For example, a page size of 10 require limit = 10. The maximum limit for this call is 50.
	 * @param offset - The index of the pagination offset. For a page size of 10, the first page would be at offset = 0, and the second page would be at offset = 10.
	 * @return
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@Deprecated
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_WITH_ENTITY_ID, method = RequestMethod.GET)
	public @ResponseBody
	PaginatedResults<AccessApproval> getEntityAccessApprovals(
				@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
				@PathVariable(value= ID_PATH_VARIABLE) String entityId,
				@RequestParam(value = ServiceConstants.PAGINATION_LIMIT_PARAM, required = false) Long limit,
				@RequestParam(value = ServiceConstants.PAGINATION_OFFSET_PARAM, required = false) Long offset
			) throws DatastoreException, UnauthorizedException, NotFoundException {
		RestrictableObjectDescriptor subjectId = new RestrictableObjectDescriptor();
		subjectId.setId(entityId);
		subjectId.setType(RestrictableObjectType.ENTITY);
		return serviceProvider.getAccessApprovalService().getAccessApprovals(userId, subjectId, limit, offset);
	}

	/**
	 * Retrieve the Access Approvals for the given Team.  This service is only available to the ACT.
	 * 
	 * @param userId
	 * @param id the Team of interest
	 * @param limit - Limits the size of the page returned. For example, a page size of 10 require limit = 10. The maximum limit for this call is 50.
	 * @param offset - The index of the pagination offset. For a page size of 10, the first page would be at offset = 0, and the second page would be at offset = 10.
	 * @return
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@Deprecated
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_WITH_TEAM_ID, method = RequestMethod.GET)
	public @ResponseBody
	PaginatedResults<AccessApproval> getTeamAccessApprovals(
				@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
				@PathVariable String id,
				@RequestParam(value = ServiceConstants.PAGINATION_LIMIT_PARAM, required = false) Long limit,
				@RequestParam(value = ServiceConstants.PAGINATION_OFFSET_PARAM, required = false) Long offset
			) throws DatastoreException, UnauthorizedException, NotFoundException {
		RestrictableObjectDescriptor subjectId = new RestrictableObjectDescriptor();
		subjectId.setId(id);
		subjectId.setType(RestrictableObjectType.TEAM);
		return serviceProvider.getAccessApprovalService().getAccessApprovals(userId, subjectId, limit, offset);
	}

	/**
	 * Retrieve the Access Approvals for the given Evaluation.  This service is only available to the ACT.
	 * 
	 * @param userId
	 * @param evaluationId the evaluation of interest
	 * @param limit - Limits the size of the page returned. For example, a page size of 10 require limit = 10. The maximum limit for this call is 50.
	 * @param offset - The index of the pagination offset. For a page size of 10, the first page would be at offset = 0, and the second page would be at offset = 10.
	 * @return
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@Deprecated
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_WITH_EVALUATION_ID, method = RequestMethod.GET)
	public @ResponseBody
	PaginatedResults<AccessApproval> getEvaluationAccessApprovals(
				@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
				@PathVariable(value= EVALUATION_ID_PATH_VAR_WITHOUT_BRACKETS) String evaluationId,
				@RequestParam(value = ServiceConstants.PAGINATION_LIMIT_PARAM, required = false) Long limit,
				@RequestParam(value = ServiceConstants.PAGINATION_OFFSET_PARAM, required = false) Long offset
			) throws DatastoreException, UnauthorizedException, NotFoundException {
		RestrictableObjectDescriptor subjectId = new RestrictableObjectDescriptor();
		subjectId.setId(evaluationId);
		subjectId.setType(RestrictableObjectType.EVALUATION);
		return serviceProvider.getAccessApprovalService().getAccessApprovals(userId, subjectId, limit, offset);
	}

	/**
	 * Delete a selected Access Approval.  This service is only available to the ACT.
	 * @param userId
	 * @param approvalId the approval to delete
	 * @throws DatastoreException
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@Deprecated
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL_WITH_APPROVAL_ID, method = RequestMethod.DELETE)
	public void deleteAccessApproval(
				@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@PathVariable String approvalId) throws DatastoreException, UnauthorizedException, NotFoundException {
		serviceProvider.getAccessApprovalService().deleteAccessApproval(userId, approvalId);
	}

	/**
	 * Revoke all Access Approvals an accessor may have for a given Access Requirement.
	 * This service is only available to the ACT.
	 * @param userId - The user who is making the request
	 * @param accessRequirementId - The access requirement to look for
	 * @param accessorId - The user whose access is being revoked
	 * @throws UnauthorizedException
	 * @throws NotFoundException
	 */
	@Deprecated
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = UrlHelpers.ACCESS_APPROVAL, method = RequestMethod.DELETE)
	public void revokeAccessApprovals(
			@RequestParam(value = AuthorizationConstants.USER_ID_PARAM) Long userId,
			@RequestParam(value = ServiceConstants.ACCESS_REQUIREMENT_ID_PARAM, required = true) String requirementId,
			@RequestParam(value = ServiceConstants.ACCESSOR_ID_PARAM, required = true) String accessorId)
					throws UnauthorizedException, NotFoundException {
		serviceProvider.getAccessApprovalService().revokeAccessApprovals(userId, requirementId, accessorId);
	}
}

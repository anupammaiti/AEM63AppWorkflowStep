package com.aem.community.core.workflow;

import java.util.Arrays;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
@Component(immediate=true, service = ReplicateViaSpecificAgent.class, property = {Constants.SERVICE_DESCRIPTION + "=Write Adaptive Form Attachments to File System",
        Constants.SERVICE_VENDOR + "=Adobe Systems","process.label=ActivateViaSpecificAgent" })
public class ReplicateViaSpecificAgent implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(ReplicateViaSpecificAgent.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments)
			throws WorkflowException {
		log.debug("The string I got was ..." + processArguments.get("PROCESS_ARGS", "string").toString());
		String[] lProcessArgs = processArguments.get("PROCESS_ARGS", "string").toString().split(",");
		log.debug("Workflow Step arguments :"+Arrays.toString(lProcessArgs));
		String lPayloadPath = workItem.getWorkflowData().getPayload().toString();
		log.debug("Workflow payload path :"+lPayloadPath);

	}

}
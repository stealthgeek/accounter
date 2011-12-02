package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ClassListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<AccounterClass>(getMessages()
				.accounterClass(), "", 20) {

			@Override
			protected String onSelection(AccounterClass value) {
				return "update class #" + value.getclassName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(getMessages().accounterClass());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(
						getMessages().accounterClass());

			}

			@Override
			protected Record createRecord(AccounterClass value) {
				Record classRec = new Record(value);
				classRec.add(getMessages().name(), value.getName());
				return classRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getMessages().accounterClass()));

			}

			@Override
			protected boolean filter(AccounterClass e, String name) {
				return false;

			}

			@Override
			protected List<AccounterClass> getLists(Context context) {
				return getClasses(context);
			}

		});

	}

	private List<AccounterClass> getClasses(Context context) {
		Set<AccounterClass> classes = context.getCompany()
				.getAccounterClasses();
		List<AccounterClass> result = new ArrayList<AccounterClass>(classes);
		return result;
	}
}

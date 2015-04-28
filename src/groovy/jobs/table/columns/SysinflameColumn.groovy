package jobs.table.columns

import com.google.common.collect.ImmutableMap

import groovy.transform.CompileStatic

import org.transmartproject.core.dataquery.clinical.ClinicalVariableColumn
import org.transmartproject.core.dataquery.clinical.PatientRow
import org.transmartproject.core.exceptions.InvalidArgumentsException

/**
 * Column that supports an arbitrary number of numeric clinical variables
 * and collects the values under a map where the keys are configurable
 * via the provided clinical variable -> string map.
 */
@CompileStatic
class SysinflameColumn extends AbstractColumn {

	/* clinical variable -> name of the group */
	Map<ClinicalVariableColumn, String> clinicalVariables

	private PatientRow lastRow

	@Override
	void onReadRow(String dataSourceName, Object row) {
		assert lastRow == null
		assert row instanceof PatientRow

		lastRow = (PatientRow) row
	}

	@Override
	Map<String, Object> consumeResultingTableRows() {
		if (lastRow == null) {
			return ImmutableMap.of()
		}

		ImmutableMap.Builder<String, Object> builder =
				ImmutableMap.builder()

		clinicalVariables.each { ClinicalVariableColumn col,
			String groupName ->
			def value = lastRow.getAt col
			if (value == groupName) {
				groupName = generateGroupNameForStrings col.getLabel()
			}
			if (value != null) {
				builder.put groupName, value
			}
		}

		PatientRow lastRowSaved = lastRow
		lastRow = null
		ImmutableMap.of(
				lastRowSaved.patient.inTrialId,
				builder.build())
	}

	/**
	 * This will generate a more superior group for strings where the VALUE == GROUP
	 * @param conceptPath
	 * @return
	 */
	private String generateGroupNameForStrings(String conceptPath) {
		/* find 2nd last non-empty segment (separated by \) */
		conceptPath.split('\\\\').findAll()[-2]
	}
	private void validateNumber(ClinicalVariableColumn col, Object value) {
	}
}

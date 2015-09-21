package jobs.steps.helpers

import com.recomdata.transmart.util.Functions
import jobs.table.Column
import jobs.table.columns.MultiNumericClinicalVariableColumn
import jobs.table.columns.SysinflameColumn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.transmartproject.core.dataquery.clinical.ClinicalVariable
import org.transmartproject.core.dataquery.clinical.ClinicalVariableColumn

@Component
@Scope('prototype')
class SysinflameMultiCategoricalClinicalVariableColumnConfigurator extends ColumnConfigurator {

    String keyForConceptPaths

    boolean pruneConceptPath = true

    GroupNamesHolder groupNamesHolder

    @Autowired
    private ClinicalDataRetriever clinicalDataRetriever

    @Autowired
    private SysinflameResultInstanceIdsHolder resultInstanceIdsHolder

    @Override
    protected void doAddColumn(Closure<Column> decorateColumn) {
        List<ClinicalVariable> variables = getConceptPaths().collect {
			println "IT: " + it
//			it = it.replaceAll("\\\\","\\\\\\\\")+"%"
//			println it
            clinicalDataRetriever.createVariableFromConceptPath it
        }
        variables = variables.collect {
            clinicalDataRetriever << it
        }
		log.warn("variablesvariablesvariables " + variables)
        clinicalDataRetriever.attachToTable table

        Map<ClinicalVariableColumn, String> variableToGroupName =
                Functions.inner(variables,
                        conceptPaths.collect { generateGroupName it },
                        { a, b -> [a,b]}).collectEntries()

        if (groupNamesHolder) {
            groupNamesHolder.groupNames = variableToGroupName.values() as List
        }

        table.addColumn(
                decorateColumn.call(
                        new SysinflameColumn(
                                clinicalVariables: variableToGroupName,
                                header:            header)),
                [ClinicalDataRetriever.DATA_SOURCE_NAME] as Set)
    }

    public List<String> getConceptPaths() {
        getStringParam(keyForConceptPaths).split(/\|/) as List
    }

    private String generateGroupName(String conceptPath) {
		        if (pruneConceptPath)  {
            /* find last non-empty segment (separated by \) */
//			log.warn(conceptPath.split('\\\\').findAll()[-1])
            conceptPath.split('\\\\').findAll()[-1]
        } else {
            conceptPath
        }
    }
}

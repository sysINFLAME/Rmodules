package jobs.steps.helpers

import jobs.table.Column
import jobs.table.columns.CategoricalVariableColumn
import jobs.table.columns.ConstantValueColumn
import jobs.table.columns.PhenomapCategoricalVariableColumn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.transmartproject.core.dataquery.clinical.ClinicalVariable

@Component
@Scope('prototype')
class PhenomapCategoricalColumnConfigurator extends ColumnConfigurator {

    String keyForConceptPaths

    @Autowired
    private ClinicalDataRetriever clinicalDataRetriever

    @Override
    protected void doAddColumn(Closure<Column> decorateColumn) {

        String conceptPaths = getConceptPaths()
		println 'conceptPaths ' + conceptPaths
        if (conceptPaths != '') {
            Set<ClinicalVariable> variables =
                    conceptPaths.split(/\|/).collect {
                        clinicalDataRetriever.createVariableFromConceptPath it.trim()
                    }

            variables = variables.collect {
                clinicalDataRetriever << it
            }

            clinicalDataRetriever.attachToTable table
			println 'header ' + header
			println 'variables ' + variables
            table.addColumn(
                    decorateColumn.call(
                            new PhenomapCategoricalVariableColumn(
                                    header:    header,
                                    leafNodes: variables)),
                    [ClinicalDataRetriever.DATA_SOURCE_NAME] as Set)
        } else {
            //optional, empty value column
            table.addColumn(new ConstantValueColumn(
                            header: header,
                            missingValueAction: missingValueAction),
                    Collections.emptySet())
        }
    }

    String getConceptPaths() {
        //if required this will fail on empty conceptPaths
        getStringParam(keyForConceptPaths, required)
    }
}

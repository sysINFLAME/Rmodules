package jobs.steps

import com.google.common.base.Function
import com.google.common.collect.Iterators
import jobs.steps.helpers.GroupNamesHolder

/**
 *  Dump data by creating column as many as selected concepts
 */
class SysinflameDiversityAnalysisDumpDataStep extends SimpleDumpTableResultStep {

    GroupNamesHolder groupNamesHolder

    private List<String> getVariableNames() {
        groupNamesHolder.groupNames
    }

    @Override
    protected List<String> getHeaders() {
        variableNames
    }

    @Override
    protected Iterator getMainRows() {
        Iterators.transform(table.result.iterator(), {
            Map<String, Object> currentMap = it[1]
            variableNames.collect { currentMap[it] }
        } as Function)
    }
}

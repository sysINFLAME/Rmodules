package jobs

import jobs.steps.Step
import jobs.steps.ValueGroupDumpDataStep
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope('job')
class Sysinflame_Diversity extends HighDimensionalOnlyJob {

    @Override
    protected Step createDumpHighDimensionDataStep(Closure resultsHolder) {
        new ValueGroupDumpDataStep(
                temporaryDirectory: temporaryDirectory,
                resultsHolder: resultsHolder,
                params: params)
    }

    @Override
    protected List<String> getRStatements() {
        String source = 'source(\'$pluginDirectory/Sysinflame/Diversity/DiversityLoader.R\')'

        String createHeatmap = '''Sysinflame_Diversity.loader(
                            input.filename = '$inputFileName',
                            aggregate.probes = '$divIndependentVariableprobesAggregation' == 'true'
                            ${ txtMaxDrawNumber ? ", maxDrawNumber  = as.integer('$txtMaxDrawNumber')" : ''},
                            calculateZscore = '$calculateZscore'
                            )'''

        [ source, createHeatmap ]
    }

    @Override
    protected getForwardPath() {
        "/sysinflame_diversity/sysinflame_diversityOut?jobName=${name}"
    }

}

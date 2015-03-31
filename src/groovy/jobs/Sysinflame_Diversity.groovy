package jobs

import jobs.steps.*
import jobs.steps.helpers.GroupNamesHolder
import jobs.steps.helpers.MultiNumericClinicalVariableColumnConfigurator
import jobs.steps.helpers.SimpleAddColumnConfigurator
import jobs.table.Table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static jobs.steps.AbstractDumpStep.DEFAULT_OUTPUT_FILE_NAME

@Component
@Scope('job')
class Sysinflame_Diversity extends AbstractAnalysisJob {
    @Autowired
    SimpleAddColumnConfigurator primaryKeyColumnConfigurator

    @Autowired
    MultiNumericClinicalVariableColumnConfigurator columnConfigurator

    @Autowired
    Table table

    GroupNamesHolder holder = new GroupNamesHolder()

    @PostConstruct
    void init() {

        columnConfigurator.header = 'VALUE'
        columnConfigurator.keyForConceptPaths = 'variablesConceptPaths'
        columnConfigurator.groupNamesHolder = holder

    }

    @Override
    protected List<Step> prepareSteps() {

        List<Step> steps = []

        steps << new ParametersFileStep(
                temporaryDirectory: temporaryDirectory,
                params: params)

        steps << new BuildTableResultStep(
                table: table,
                configurators: [columnConfigurator])

        steps << new CorrelationAnalysisDumpDataStep(
                table: table,
                temporaryDirectory: temporaryDirectory,
                groupNamesHolder:   holder,
                outputFileName: DEFAULT_OUTPUT_FILE_NAME)

        steps << new RCommandsStep(
                temporaryDirectory: temporaryDirectory,
                scriptsDirectory: scriptsDirectory,
                rStatements: RStatements,
                studyName: studyName,
                params: params,
                extraParams: [inputFileName: DEFAULT_OUTPUT_FILE_NAME])

        steps
    }

    
    @Override
    protected List<String> getRStatements() {
	log.warn('GETRGETR')
        [    '''Diversity.loader(
             input.filename = '$inputFileName',
             aggregate.probes = '$divIndependentVariableprobesAggregation' == 'true'
             ${ txtMaxDrawNumber ? ", maxDrawNumber  = as.integer('$txtMaxDrawNumber')" : ''},
             calculateZscore = '$calculateZscore'
             )'''
        ]
    }

    @Override
    protected getForwardPath() {
	log.warn('FORWARDPATH')
    	 "/sysinflame_diversity/sysinflame_diversityOut?jobName=${name}"
    }
}

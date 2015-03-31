package jobs

import jobs.steps.*
import jobs.steps.helpers.GroupNamesHolder
import jobs.steps.helpers.NumericClinicalVariableColumnConfigurator
import jobs.steps.helpers.SimpleAddColumnConfigurator
import jobs.table.Table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import jobs.table.columns.PrimaryKeyColumn
import javax.annotation.PostConstruct
import jobs.steps.SimpleDumpTableResultStep

import static jobs.steps.AbstractDumpStep.DEFAULT_OUTPUT_FILE_NAME

@Component
@Scope('job')
class SysinflameDiversity extends AbstractAnalysisJob {
    @Autowired
    SimpleAddColumnConfigurator primaryKeyColumnConfigurator

    
    @Autowired
    NumericClinicalVariableColumnConfigurator columnConfigurator

    @Autowired
    Table table

    GroupNamesHolder holder = new GroupNamesHolder()

    @PostConstruct
    void init() {
    	primaryKeyColumnConfigurator.column = new PrimaryKeyColumn(header: 'PATIENT_NUM')
        columnConfigurator.header = 'VALUE'
        columnConfigurator.keyForConceptPaths = 'variablesCensorConceptPaths'
        
        
        
    }

    @Override
    protected List<Step> prepareSteps() {

        List<Step> steps = []

        steps << new ParametersFileStep(
                temporaryDirectory: temporaryDirectory,
                params: params)

        steps << new BuildTableResultStep(
                table: table,
                configurators: [primaryKeyColumnConfigurator,columnConfigurator])
//
        
        steps << new SimpleDumpTableResultStep(table: table,
                temporaryDirectory: temporaryDirectory,
                outputFileName: DEFAULT_OUTPUT_FILE_NAME
        )
        
        
//        steps << new CorrelationAnalysisDumpDataStep(
//                table: table,
//                temporaryDirectory: temporaryDirectory,
//                groupNamesHolder:   holder,
//                outputFileName: DEFAULT_OUTPUT_FILE_NAME)
//        steps << new RCommandsStep(
//                temporaryDirectory: temporaryDirectory,
//                scriptsDirectory: scriptsDirectory,
//                rStatements: RStatements,
//                studyName: studyName,
//                params: params,
//                extraParams: [inputFileName: DEFAULT_OUTPUT_FILE_NAME])

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
    	 "/sysinflameDiversity/sysinflameDiversityOut?jobName=${name}"
    }
}

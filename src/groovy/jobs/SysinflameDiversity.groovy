package jobs

import jobs.steps.*
import jobs.steps.helpers.GroupNamesHolder
import jobs.steps.helpers.MultiNumericClinicalVariableColumnConfigurator
import jobs.steps.helpers.SimpleAddColumnConfigurator
import jobs.table.Table
import jobs.table.columns.PrimaryKeyColumn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static jobs.steps.AbstractDumpStep.DEFAULT_OUTPUT_FILE_NAME


@Component
@Scope('job')
class SysinflameDiversity extends AbstractAnalysisJob {
    
	@Autowired
    SimpleAddColumnConfigurator primaryKeyColumnConfigurator

    @Autowired
    MultiNumericClinicalVariableColumnConfigurator columnConfigurator
	
	@Autowired
	MultiNumericClinicalVariableColumnConfigurator columnConfigurator2
	
    @Autowired
    Table table

    GroupNamesHolder holder = new GroupNamesHolder()

    @PostConstruct
    void init() {
		
    	primaryKeyColumnConfigurator.column = new PrimaryKeyColumn(header: 'PATIENT_NUM')

		columnConfigurator2.header = 'VALUE'
		columnConfigurator2.keyForConceptPaths = 'variablesCensorConceptPaths'
		columnConfigurator2.groupNamesHolder = holder
		
		
		columnConfigurator.header = 'MICROBIOME'
		columnConfigurator.keyForConceptPaths = 'variablesMicrobiomConceptPaths'
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
                configurators: [primaryKeyColumnConfigurator,columnConfigurator,columnConfigurator2]) //categoryVariableConfigurator,categoryVariableConfigurator
//
        steps << new SimpleDumpTableResultStep(
                table: table,
                temporaryDirectory: temporaryDirectory,
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
        [   
			'''source('$pluginDirectory/Sysinflame/Diversity/DiversityLoader.r')''',
			 '''Diversity.loader(
             input.filename = '$inputFileName',
             )'''
        ]
    }

    @Override
    protected getForwardPath() {
	log.warn('FORWARDPATH')
    	 "/sysinflameDiversity/sysinflameDiversityOutput?jobName=$name"
    }
}

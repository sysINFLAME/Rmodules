%{--include js lib for heatmap dynamically--}%
<r:require modules="sysinflameDiversity"/>
<r:layoutResources disposition="defer"/>

<div id="analysisWidget">

    <h2>
        Variable Selection
        <a href='JavaScript:D2H_ShowHelp(1505,helpURL,"wndExternal",CTXT_DISPLAY_FULLHELP )'>
            <img src="${resource(dir: 'images', file: 'help/helpicon_white.jpg')}" alt="Help"/>
        </a>
    </h2>

                       

<form id="analysisForm">
        <div class="container ">


            %{-- ************************************************************************************************* --}%
            %{-- left inputs --}%
            %{-- ************************************************************************************************* --}%
            <div class="left">
                <fieldset class="inputFields">
                    <div class="highDimContainer">
                        <h3>Microbiom</h3>
                        <div class="divInputLabel">Select a microbiome on which you would like to do the analysis and
                        drag it into the box. This variable is required.</div>
                        <div id='divDataNode' class="queryGroupIncludeLong divInputBox"></div>
                        <div class="highDimBtns">
                           %{-- <button type="button" onclick="highDimensionalData.gather_high_dimensional_data('divDataNode', true)">High Dimensional Data</button> --}%
                            <button type="button" onclick="sysinflameDiversityView.clear_high_dimensional_input('divDataNode')">Clear</button>
                        </div>
                        <input type="hidden" id="dependentVarDataType">
                        <input type="hidden" id="dependentPathway">
                    </div>

                    %{--Display independent variable--}%
                    <div id="displaydivCategoryVariable" class="independentVars"></div>

                   
                </fieldset>
            </div>

            %{-- ************************************************************************************************* --}%
            %{-- right inputs --}%
            %{-- ************************************************************************************************* --}%
            <div class="right">
                <fieldset class="inputFields">
                    <h3>Variable</h3>
                    <div class="divInputLabel">Select the appropriate numeric variable and drag it into the box. For
                    example, "BMI".
                    This variable is  required.	</div>
                    <div id='divCategoryVariable' class="queryGroupIncludeLong divInputBox"></div>
                    <div class="highDimBtns">
                        <button type="button" onclick="sysinflameDiversityView.clear_high_dimensional_input('divCategoryVariable')">Clear</button>
                    </div>
                </fieldset>
            </div>
        </div>

 %{-- ************************************************************************************************* --}%
        %{-- Tool Bar --}%
        %{-- ************************************************************************************************* --}%
        <fieldset class="toolFields">
            <input type="button" value="Run" onClick="sysinflameDiversityView.submit_job(this.form);" class="runAnalysisBtn">
        </fieldset>
    </form>

</div>


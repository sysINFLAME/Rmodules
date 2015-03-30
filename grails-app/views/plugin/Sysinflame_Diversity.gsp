%{--include js lib for heatmap dynamically--}%
<r:require modules="sysinflame_diversity"/>
<r:layoutResources disposition="defer"/>

<div id="analysisWidget">

    <h2>
        Variable Selection
        <a href='JavaScript:D2H_ShowHelp(1505,helpURL,"wndExternal",CTXT_DISPLAY_FULLHELP )'>
            <img src="${resource(dir: 'images', file: 'help/helpicon_white.jpg')}" alt="Help"/>
        </a>
    </h2>

                       

<form id="analysisForm">
        <div class="three-layout-container ">


            %{-- ************************************************************************************************* --}%
            %{-- left inputs --}%
            %{-- ************************************************************************************************* --}%
            <div class="three-layout-left">
                <fieldset class="inputFields">
                    <div class="highDimContainer">
                        <h3>Metabolom</h3>
                        <div class="divInputLabel">Select a matabolom on which you would like to do the analysis and
                        drag it into the box. This variable is required.</div>
                        <div id='divCategoryVariable' class="queryGroupIncludeLong divInputBox"></div>
                        <div class="highDimBtns">
                            <button type="button" onclick="highDimensionalData.gather_high_dimensional_data('divCategoryVariable', true)">High Dimensional Data</button>
                            <button type="button" onclick="sysinflame_DiversityView.clear_high_dimensional_input('divCategoryVariable')">Clear</button>
                        </div>
                        <input type="hidden" id="dependentVarDataType">
                        <input type="hidden" id="dependentPathway">
                    </div>

                    %{--Display independent variable--}%
                    <div id="displaydivCategoryVariable" class="independentVars"></div>

                   
                </fieldset>
            </div>

            %{-- ************************************************************************************************* --}%
            %{-- middle inputs --}%
            %{-- ************************************************************************************************* --}%
            <div class="three-layout-middle">
                <fieldset class="inputFields">
                    <h3>Variable</h3>
                    <div class="divInputLabel">Select the appropriate censoring variable and drag it into the box. For
                    example, "Survival (Censor) -> Yes".
                    This variable is not required.	</div>
                    <div id='divCensoringVariable' class="queryGroupIncludeLong divInputBox"></div>
                    <div class="highDimBtns">
                        <button type="button" onclick="sysinflame_DiversityView.clear_high_dimensional_input('divCensoringVariable')">Clear</button>
                    </div>
                </fieldset>
            </div>
        </div>

        %{-- ************************************************************************************************* --}%
        %{-- Tool Bar --}%
        %{-- ************************************************************************************************* --}%
        <fieldset class="toolFields">
            <div class="chkpair">
                <g:checkBox name="isBinning" onclick="sysinflame_DiversityView.toggle_binning();"/> Enable binning
            </div>
            <input type="button" value="Run" onClick="sysinflame_DiversityView.submit_job(this.form);" class="runAnalysisBtn">
        </fieldset>
    </form>

</div>


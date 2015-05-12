##@Author Carolin Knecht, Institut für Medizinische Informatik und Statistik, Universitätsklinikum Schleswig-Holstein Kiel
##@Author Benjamin Baum, Department of Medical Informatics, Göttingen, <benjamin.baum@med.uni-goettingen.de>
##@Author Malte Rühlemann, Institute of Clinical Molecular Biology, Christian-Albrechts-University Kiel


## Ohne Wert für Input Mode -> Shannon als Standard; auch wenn input mode nicht in der u.g. liste
## Alternativen für Input.Mode: "shannon", "simpson", "invsimpson", "chao1", "obs" oder "ACE"

Diversity.loader <- function(
  input.filename,
  input.mode='shannon', 
  output.file="Diversity",
  output.name="Diversity"
)
{

  ######################################################
  library(Cairo)
  library(vegan)
  library(ggplot2)
  ######################################################
  
input<-read.delim(input.filename,header=T)
  
  ##Erstellen der Column-Row-Matrix
  Microbiom <- input[,2]
  summary(Microbiom)
  Microbiom <- gsub("[:{}:]", "", Microbiom)
  
  Bakterienanzahl <- length(unlist(strsplit(Microbiom[1], ",")))
  
  Microbiom_table <-as.matrix(t(unlist(strsplit(Microbiom[1], ","))))
  for (k in 1:Bakterienanzahl)
  {
    Microbiom_table[1,k]<- gsub("\\s", "", as.character(Microbiom_table[1,k])  ) 
  }
  
  for (i in 2:length(Microbiom) )
  {
    Microbiom_Vektor <- unlist((strsplit(Microbiom[i], ",")))
    
    for(k in 1:Bakterienanzahl)
    {
      Microbiom_Vektor[k] <- gsub("\\s", "", as.character(Microbiom_Vektor[k]))
    }
    Microbiom_table <- rbind(Microbiom_table,Microbiom_Vektor)
  }
  Microbiom_table_names <- Microbiom_table
  Microbiom_names<- rep("keinName",Bakterienanzahl)
  
  for(k in 1:Bakterienanzahl)
  {
    Microbiom_Vektor3 <- unlist((strsplit(Microbiom_table[1,k], "=")))
    Microbiom_names[k]<-Microbiom_Vektor3[1]
  }
  
  for(i in 1:length(Microbiom))
  {
    for(k in 1:Bakterienanzahl)
    {
      Microbiom_Vektor2 <- unlist((strsplit(Microbiom_table[i,k], "=")))
      Microbiom_table[i,k]<-Microbiom_Vektor2[2]
    }
  }
  
  Microbiom_table <- matrix(as.numeric(Microbiom_table), nrow=length(Microbiom), ncol=Bakterienanzahl)
  
  colnames(Microbiom_table)<-Microbiom_names
  rownames(Microbiom_table)<-input[,1]
  
  ##Welcher Diversity Modus soll verwendet werden? Shannon oder Simpson?
  ### Default von "diversity()" ist shannon 
  #Berechnen des diversity index abhängig von input.mode
  
switch(input.mode, 
	shannon={
  		mode.main="Alpha-diversity Shannon-Index"
      	mode.ylab="Shannon-Index"
      	div <- diversity(Microbiom_table, index = input.mode)
},
	simpson={
  		mode.main="Alpha-diversity Simpson-Index"
      	mode.ylab="Simpson-Index"
      	div <- diversity(Microbiom_table, index = input.mode)
},
	invsimpson={
  		mode.main="Alpha-diversity Inverse-Simpson-Index"
      	mode.ylab="Inverse-Simpson-Index"
      	div <- diversity(Microbiom_table, index = input.mode)
},
	chao1={
    	mode.main="Alpha-diversity Chao1-Estimator"
      	mode.ylab="Chao1-Estimator"
      	div <- estimateR(Microbiom_table)[2,]
},
	obs={
    	mode.main="Observed Species"
      	mode.ylab="Observed Species"
      	div <- estimateR(Microbiom_table)[1,]
},	
	ACE={
    	mode.main="Alpha-diversity ACE-Estimator"
      	mode.ylab="ACE-Estimator"
      	div <- estimateR(Microbiom_table)[4,]
},
{
    	mode.main="Alpha-diversity Shannon-Index"
      	mode.ylab="Shannon-Index"
      	div <- diversity(Microbiom_table, index = "shannon")
}
)

  CairoPNG(file=paste("Bild1",".png",sep=""), width=800, height=800,units = "px")  
  
  barplot(div, las=2, cex.names=0.5, col="steelblue", main=mode.main,ylab=mode.ylab)
  
  dev.off()

  Metadaten <- data.frame(input[,3],div)
  
  colnames(Metadaten)<-c("meta","shannon")
  
  p1<-qplot(Metadaten$meta, Metadaten$shannon, data = Metadaten, geom = c( "jitter", "boxplot"), colour=I("black"), fill=I("lightgreen"), main= mode.main, face="bold",ylab=mode.ylab, xlab="")
  
  

  CairoPNG(file=paste("Bild2",".png",sep=""), width=800, height=800,units = "px")  
  
  print(p1 + theme_bw())
  
  dev.off()
}


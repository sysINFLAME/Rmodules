######
#R- Code zur Erkennung von Ausreissern zur Implementierung in TranSMART
#
#Carolin Knecht (2015)
#
#Weiterabgabe an Dritte bitte nur nach Absprache!
######

Extremwerte.loader <- function(
  input.filename,
  output.file="Extremwerte",
  output.name="Extremwerte"
)
{
#benoetigte Pakete
library(Cairo)
library(gridExtra)
library(outliers) 
library(calibrate)
#Daten werten eingelesen
input<-read.delim(input.filename,header=T)


Spalten <- dim(input)[2]
#Spaltenname #Extremwerte #Methode #Bemerkung
Ausgabe <- data.frame(Item="",Samples="",Extreme_Values="",Method="",Remark="")

for(l in 2:Spalten)
{  
Werte <- input[,l]
sortiert <- sort(Werte)
Sample <- input[,1]
Eingabe <- data.frame(Sample,Werte)
Anzahl <- length(Werte)

#Je nachdem wieviele Werte vorliegen, werden unterschiedliche Tests durchgefuehrt
if(Anzahl<3)
{  Method <- "Nicht_sinnvoll"}
if(Anzahl>=3 && Anzahl <= 8)
{  Method <- "Dixon"}
if(Anzahl>8 && Anzahl <= 25)
{  Method <- "Grubbs"}
if(Anzahl>=25)
{ Method <- "Standard_Extemwerte"}


switch(Method, 
       Grubbs={
         pWert <-0
         #In jedem Durchlauf werden die Ausreisser rausgeschmissen, bis keine mehr drin sind
         while(pWert<=0.05 && sum(sortiert)!=0 )
         {  
           GrubbsTest <- grubbs.test(sortiert)
           pWert <- GrubbsTest$p.value
           if(pWert<=0.05&& sum(sortiert)!=0)
           {
             GrubbsTest.Ausreisser <- GrubbsTest$alternative
             Wert <- strsplit(GrubbsTest.Ausreisser," ")[[1]]
             Ausreisser <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreisser)] 
             }
            }
         if(sum(sortiert)==0)
         {
           Remark <- "Only Zeros left!" ###"Achtung, nur noch Nullen in den Daten!" 
         } else
         {
           Remark <-""
         }
       },
      	
       Dixon={
         pWert <-0
         i <- 0
         while(pWert<=0.05  && sum(sortiert)!=0 )
         {  
           DixonTest <- dixon.test(sortiert)
           pWert <- DixonTest$p.value
           i <- i+1
           if(pWert<=0.05  && sum(sortiert)!=0 )
           {
             DixonTest.Ausreisser <- DixonTest$alternative
             Wert <- strsplit(DixonTest.Ausreisser," ")[[1]]
             Ausreisser <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreisser)] 
           }
           
         }
         if(sum(sortiert)==0)
         {
           Remark <- "Only Zeros left!" ###"Achtung, nur noch Nullen in den Daten!" 
         }         else
         {
           Remark <-""
         }
       },
       Standard_Extemwerte={
         Median <- median(sortiert)
         Median.Deviation <- mad(sortiert,constant=1)
         Ausreisser <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
         Ausreisser2 <- sortiert[Ausreisser]
         sortiert <- sortiert[!sortiert==as.numeric(Ausreisser2)] 
         Method <- "Standardized extreme value deviation" ###"Standardisierte Extremwertabweichung"
         if(sum(sortiert)==0)
         {
           Remark <- "Only Zeros left!" ###"Achtung, nur noch Nullen in den Daten!" 
         }         else
         {
           Remark <-""
         }
         },
       Nicht_sinnvoll={
         Method <- "Not applicable" ###"Keine Anwendung"
         Remark <- "No senseful analysis possible" ###"Keine sinnvolle Auswertung moeglich"
       },
       {
        Median <- median(sortiert)
        Median.Deviation <- mad(sortiert,constant=1)
        Ausreisser <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
        Ausreisser2 <- sortiert[Ausreisser]
        sortiert <- sortiert[!sortiert==as.numeric(Ausreisser2)] 
        Method <- "Standardized extreme value deviation" ###"Standardisierte Extremwertabweichung"
        if(sum(sortiert)==0)
        {
          Remark <- "Only Zeros left!" ###"Achtung, nur noch Nullen in den Daten!" 
        }        else
        {
          Remark <-""
        }
       }
)

#Extremwerte werden bestimmt
welche.extremwerte <- which(is.element(Werte,sortiert))
extremwerte<-Werte[-welche.extremwerte]

Extreme_Values<- paste(extremwerte,collapse=",")
laenge <- length(extremwerte)

if(laenge==0)
{
  Extreme_Values = "No Outlier" ###"Keine Ausreisser"
}

Item <-colnames(input[l])
Patienten <- 1:17

#Hier kommt der Plotaufruf
if(laenge!=0)
{
Samples <- Eingabe[Eingabe$Werte  %in% extremwerte,c("Sample")]
Samples <- paste(Samples,collapse=",")
Sampleplot <-Eingabe[Eingabe$Werte  %in% extremwerte,c("Sample")] 

CairoPNG(file=paste("Bild",Item,".png",sep=""), width=800, height=400,units = "px")  
welche.Werte_ohneExtrem <- which(is.element(Werte,extremwerte))
Werte_ohneExtrem<-Werte[-welche.Werte_ohneExtrem]
stripchart(Werte,cex=2 ,pch=16,col="white",method="jitter", xlab=Item, cex.lab=1.5)
stripchart(Werte_ohneExtrem,cex=2 ,pch=16,col="steelblue",method="jitter", add=T)
stripchart(extremwerte, cex=2, pch=16,method="stack", col="darkred", add=T)
n <- 1.05
p <- (range(Werte)[2]-range(Werte)[1])/15
for(i in 1:length(extremwerte))
{
textxy(extremwerte[i]-p, n,Sampleplot[i], cx=0.8)
n <- n-0.05
}
dev.off()

} else 
{
  Samples <- ""
  CairoPNG(file=paste("Bild",Item,".png",sep=""), width=800, height=400,units = "px")  
  stripchart(Werte,cex=2 ,pch=16,col="steelblue",method="jitter", xlab=Item, cex.lab=1.5)
  dev.off()
}



Ausgabe_einzel <- data.frame(Item,Samples,Extreme_Values,Method,Remark)
Ausgabe <- rbind(Ausgabe,Ausgabe_einzel)
#Hier wird das ganze Ausgegeben
}
Ausgabe <- Ausgabe[-1,]
rownames(Ausgabe) <- Ausgabe$Item
write.table(Ausgabe,file="Ausgabe.csv", sep=";", dec=",", row.names=F,quote=F)
Ausgabe <- Ausgabe[,-1]
pdf("Ausgabe.pdf", 10,Spalten-4)
grid.table(Ausgabe)
dev.off()
}

######
#R- Code zur Erkennung von Ausreiﬂern zur Implementierung in TranSMART
#
#Programmiert von Carolin Knecht (2015)
#
#
######

Extremwerte.loader <- function(
  input.filename,
  Spaltenname,
  output.file="Extremwerte",
  output.name="Extremwerte"
)
{
  
library(outliers) 
#Daten werten eingelesen
input<-read.delim(input.filename,header=T)
Werte <- input[[Spaltenname]]
sortiert <- sort(Werte)
Sample <- input[,1]
Eingabe <- data.frame(Sample,Werte)
Anzahl <- length(Werte)

#Je nachdem wieviele Werte vorliegen, werden unterschiedliche Tests durchgef¸hrt
if(Anzahl<3)
{  Methode <- "Nicht_sinnvoll"}
if(Anzahl>=3 && Anzahl <= 8)
{  Methode <- "Dixon"}
if(Anzahl>8 && Anzahl <= 25)
{  Methode <- "Grubbs"}
if(Anzahl>=25)
{ Methode <- "Standard_Extemwerte"}


switch(Methode, 
       Grubbs={
         pWert <-0
         #In jedem Durchlauf werden die Ausreiﬂer rausgeschmissen, bis keine mehr drin sind
         while(pWert<=0.05 && sum(sortiert)!=0 )
         {  
           GrubbsTest <- grubbs.test(sortiert)
           pWert <- GrubbsTest$p.value
           if(pWert<=0.05&& sum(sortiert)!=0)
           {
             GrubbsTest.Ausreiﬂer <- GrubbsTest$alternative
             Wert <- strsplit(GrubbsTest.Ausreiﬂer," ")[[1]]
             Ausreiﬂer <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer)] 
             }
            }
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten" 
         } else
         {
           Bemerkung <-""
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
             DixonTest.Ausreiﬂer <- DixonTest$alternative
             Wert <- strsplit(DixonTest.Ausreiﬂer," ")[[1]]
             Ausreiﬂer <- Wert[3]
             sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer)] 
           }
           
         }
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten" 
         }         else
         {
           Bemerkung <-""
         }
       },
       Standard_Extemwerte={
         Median <- median(sortiert)
         Median.Deviation <- mad(sortiert,constant=1)
         Ausreiﬂer <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
         Ausreiﬂer2 <- sortiert[Ausreiﬂer]
         sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer2)] 
         Methode <- "Standardisierte Extremwertabweichung"
         if(sum(sortiert)==0)
         {
           Bemerkung <- "Achtung, nur noch Nullen in den Daten" 
         }         else
         {
           Bemerkung <-""
         }
         },
       Nicht_sinnvoll={
         Methode <- "Keine Anwendung"
         Bemerkung <-"Keine sinnvolle Auswertung mˆglich"
       },
       {
        Median <- median(sortiert)
        Median.Deviation <- mad(sortiert,constant=1)
        Ausreiﬂer <- (sortiert < Median - 5.2*Median.Deviation) | (sortiert > Median + 5.2*Median.Deviation)
        Ausreiﬂer2 <- sortiert[Ausreiﬂer]
        sortiert <- sortiert[!sortiert==as.numeric(Ausreiﬂer2)] 
        Methode <- "Standardisierte Extremwertabweichung"
        if(sum(sortiert)==0)
        {
          Bemerkung <- "Achtung, nur noch Nullen in den Daten" 
        }        else
        {
          Bemerkung <-""
        }
       }
)

#Extremwerte werden bestimmt
extremwerte<-setdiff(Werte,sortiert)
paste(extremwerte)
laenge <- length(extremwerte)

if(laenge==0)
{
  extremwerte = "Keine Ausreiﬂer"
}
  
if(laenge!=0)
{
Samples <- Eingabe[Eingabe$Werte  %in% extremwerte,c("Sample","Werte")]
colnames(Samples) <- c("Sample","Extremwerte")
} else 
{
  Samples <- data.frame(Samples="",Extremwerte=extremwerte)
}

Ausgabe <- data.frame(Samples,Methode,Bemerkung)
#Hier wird das ganze Ausgegeben
write.table(Ausgabe,file="Ausgabe.csv", sep=";", dec=",", row.names=F,quote=F)



}



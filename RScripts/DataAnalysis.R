data <- read.csv("FastClassiferData.csv")
library(lattice)
with(data, xyplot(AvgKeyPointY ~ AvgKeyPointX, group=Symbol))
with(data, xyplot(AvgKeyPointY ~ AvgKeyPointX, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(AvgKeyPointX, AvgKeyPointY, ...);
  ltext(x=AvgKeyPointX, y=AvgKeyPointY, labels=Symbol, pos=1, offset=1, cex=0.8)
}))


with(data, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol))
with(data, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(KeyPoint1X, KeyPoint1Y, ...);
  ltext(x=KeyPoint1X, y=KeyPoint1Y, labels=Symbol, pos=1, offset=1, cex=0.8)
}))


with(data, xyplot(KeyPoint2Y ~ KeyPoint2X, group=Symbol, panel=function(x, y, ...) {
  panel.xyplot(KeyPoint2X, KeyPoint2Y, ...);
  ltext(x=KeyPoint2X, y=KeyPoint2Y, labels=Symbol, pos=1, offset=1, cex=0.8)
}))

qdata = data[which(data$Symbol == 'q' | data$Symbol == 'Q'),]
with(qdata, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol))

nNdata = data[which(data$Symbol == 'n' | data$Symbol == 'N'),]
with(nNdata, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol))

ndata = data[which(data$Symbol == 'n'),]
with(ndata, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol))

Ndata = data[which(data$Symbol == 'N'),]
with(Ndata, xyplot(KeyPoint1Y ~ KeyPoint1X, group=Symbol))

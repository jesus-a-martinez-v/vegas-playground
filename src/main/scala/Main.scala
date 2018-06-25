import vegas.DSL.OptArg
import vegas._
import vegas.data.External.{Cars, Movies, Stocks}

object Main extends App {
  val areaChart = Vegas("Simple Area Chart", width = OptArg(800), height = OptArg(600))
    .withURL(Cars)
    .mark(Area)
    .encodeX("Acceleration", Quantitative, bin = Bin())
    .encodeY("Horsepower", Quantitative, AggOps.Mean, enableBin = false)
    .encodeColor(field = "Cylinders", dataType = Nominal)

  val scatterplot = Vegas("Simple Binned Scatterplot", width = OptArg(800), height = OptArg(600))
      .withURL(Movies)
      .mark(Point)
      .encodeX("IMDB_Rating", Quantitative, bin = Bin(maxbins = 10.0))
      .encodeY("Rotten_Tomatoes_Rating", Quantitative, bin = Bin(maxbins = 10.0))
      .encodeSize(aggregate = AggOps.Count, field = "*", dataType = Quantitative)

  val binnedColorCodingScatterplot = Vegas("Sample Scatterplot", width = OptArg(800), height = OptArg(600))
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quantitative)
      .encodeY("Miles_per_Gallon", Quantitative)
      .encodeColor(field = "Acceleration", dataType = Quantitative, bin = Bin(maxbins = 5.0))

  val lineChart = Vegas("Sample Multi Series Line Chart", width = OptArg(800), height = OptArg(600))
      .withURL(Stocks, formatType = DataFormat.Csv)
      .mark(Line)
      .encodeX("date", Temp)
      .encodeY("price", Quant)
      .encodeColor(field = "symbol", dataType = Nominal, legend = Legend(orient = "left", title = "Stock Symbol"))
      .encodeDetailFields(Field(field = "symbol", dataType = Nominal))

  // areaChart.show
  // scatterplot.show
  // binnedColorCodingScatterplot.show
  lineChart.show
}

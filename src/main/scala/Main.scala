import vegas._
import vegas.data.External._

import scala.io.StdIn

object Main extends App {

  val examples = Array(
    Vegas("Simple Area Chart", width = 800.0, height = 600.0)
      .withURL(Cars)
      .mark(Area).encodeX("Acceleration", Quantitative, bin = Bin())
      .encodeY("Horsepower", Quantitative, AggOps.Mean, enableBin = false)
      .encodeColor(field = "Cylinders", dataType = Nominal),
    Vegas("Simple Binned Scatterplot", width = 800.0, height = 600.0)
      .withURL(Movies)
      .mark(Point)
      .encodeX("IMDB_Rating", Quantitative, bin = Bin(maxbins = 10.0))
      .encodeY("Rotten_Tomatoes_Rating", Quantitative, bin = Bin(maxbins = 10.0))
      .encodeSize(aggregate = AggOps.Count, field = "*", dataType = Quantitative),
    Vegas("Sample Scatterplot", width = 800.0, height = 600.0)
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quantitative)
      .encodeY("Miles_per_Gallon", Quantitative)
      .encodeColor(field = "Acceleration", dataType = Quantitative, bin = Bin(maxbins = 5.0)),
    Vegas("Sample Multi Series Line Chart", width = 800.0, height = 600.0)
      .withURL(Stocks, formatType = DataFormat.Csv)
      .mark(Line)
      .encodeX("date", Temp)
      .encodeY("price", Quant)
      .encodeColor(field = "symbol", dataType = Nominal, legend = Legend(orient = "left", title = "Stock Symbol")),
    Vegas("A simple bar chart with embedded data.")
      .withData(Seq(
        Map("a" -> "A", "b" -> 28), Map("a" -> "B", "b" -> 55), Map("a" -> "C", "b" -> 43),
        Map("a" -> "D", "b" -> 91), Map("a" -> "E", "b" -> 81), Map("a" -> "F", "b" -> 53),
        Map("a" -> "G", "b" -> 19), Map("a" -> "H", "b" -> 87), Map("a" -> "I", "b" -> 52)
      ))
      .encodeX("a", Ordinal)
      .encodeY("b", Quantitative)
      .mark(Bar),
    Vegas("A bar chart showing the US population distribution of age groups in 2000.")
      .withURL(Population)
      .mark(Bar)
      .filter("datum.year == 2000")
      .encodeY("age", Ordinal, scale = Scale(bandSize = 17.0))
      .encodeX("people", Quantitative, aggregate = AggOps.Sum, axis = Axis(title = "population")),
    Vegas()
      .withURL(Population)
      .mark(Bar)
      .filter("datum.year == 2000")
      .addTransformCalculation("gender", """datum.sex == 2 ? "Female" : "Male" """)
      .encodeColumn("age", Ord, scale = Scale(padding = 4.0), axis = Axis(orient = Orient.Bottom, axisWidth = 1.0, offset = -8.0))
      .encodeY("people", Quantitative, aggregate = AggOps.Sum, axis = Axis(title = "population", grid = false))
      .encodeX("gender", Nominal, scale = Scale(bandSize = 6.0), hideAxis = true)
      .encodeColor("gender", Nominal, scale = Scale(rangeNominals = List("#EA98D2", "#659CCA")))
      .configFacet(cell = CellConfig(strokeWidth = 0.0)),
    Vegas()
      .withURL(Unemployment)
      .mark(Area)
      .encodeX("date", Temp, timeUnit = TimeUnit.Yearmonth, scale = Scale(nice = Nice.Month), axis = Axis(axisWidth = 0.0, format = "%Y", labelAngle = 0.0))
      .encodeY("count", Quantitative, aggregate = AggOps.Sum)
      .configCell(width = 300.0, height = 200.0),
    Vegas()
      .withURL(Population)
      .filter("datum.year == 2000")
      .addTransform("gender", """datum.sex == 2 ? "Female" : "Male"""")
      .mark(Bar)
      .encodeY("people", Quant, AggOps.Sum, axis = Axis(title = "population"))
      .encodeX("age", Ord, scale = Scale(bandSize = 17.0))
      .encodeColor("gender", Nominal, scale = Scale(rangeNominals = List("#EA98D2", "#659CCA")))
      .configMark(stacked = StackOffset.Normalize),
    Vegas("A trellis scatterplot showing Horsepower and Miles per gallons, faceted by binned values of Acceleration.")
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quantitative)
      .encodeY("Miles_per_Gallon", Quantitative)
      .encodeRow("Acceleration", Quantitative, enableBin = true),
    Vegas()
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quantitative)
      .encodeY("Miles_per_Gallon", Quantitative)
      .encodeColor(field = "Origin", dataType = Nominal),
    Vegas("The Trellis display by Becker et al. helped establish small multiples as a “powerful mechanism for understanding interactions in studies of how a response depends on explanatory variables”. Here we reproduce a trellis of Barley yields from the 1930s, complete with main-effects ordering to facilitate comparison.")
      .withURL(Barley)
      .mark(Point)
      .encodeRow("site", Ordinal)
      .encodeX("yield", Quantitative, aggregate = AggOps.Mean)
      .encodeY("variety", Ordinal, sortField = Sort("yield", AggOps.Mean), scale = Scale(bandSize = 12.0))
      .encodeColor(field = "year", dataType = Nominal),
    Vegas("A scatterplot with custom star shapes.")
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quant)
      .encodeY("Miles_per_Gallon", Quant)
      .encodeColor("Cylinders", Nom)
      .encodeSize("Weight_in_lbs", Quant)
      .configMark(customShape = "M0,0.2L0.2351,0.3236 0.1902,0.0618 0.3804,-0.1236 0.1175,-0.1618 0,-0.4 -0.1175,-0.1618 -0.3804,-0.1236 -0.1902,0.0618 -0.2351,0.3236 0,0.2Z"),
    Vegas("A scatterplot showing average horsepower and displacement for cars from different origins.")
      .withURL(Cars)
      .mark(Point)
      .encodeX("Horsepower", Quant, AggOps.Mean)
      .encodeY("Displacement", Quant, AggOps.Mean)
      .encodeDetail("Origin"),
    Vegas("Stock prices of 5 Tech Companies Over Time.")
      .withURL(Stocks, formatType = DataFormat.Csv)
      .mark(Line)
      .encodeX("date", Temp)
      .encodeY("price", Quant)
      .encodeDetailFields(Field(field = "symbol", dataType = Nominal)),
    Vegas("Plot with hard-coded size value")
      .withURL(Cars)
      .mark(Circle)
      .encodeY("Horsepower", Quantitative)
      .encodeX("Miles_per_Gallon", Quantitative)
      .encodeSize(value = 201L),
    Vegas
      .layered("Plots both mean and IQR as a background layer")
      .withURL(Population)
      .withLayers(
        Layer()
          .mark(Line)
          .encodeX("age", Ordinal)
          .encodeY("people", aggregate = AggOps.Mean),
        Layer()
          .mark(Area)
          .encodeX("age", Ordinal)
          .encodeY("people", aggregate = AggOps.Q1)
          .encodeY2("people", aggregate = AggOps.Q3)
      ),
    Vegas("Plot with legend on the left and a different title ")
      .withURL(Cars)
      .mark(Point)
      .encodeY("Horsepower", Quantitative)
      .encodeX("Miles_per_Gallon", Quantitative)
      .encodeColor(field = "Origin", dataType = Nominal, legend = Legend(orient = "left", title = "Place of Origin"))
      .encodeShape(field = "Origin", dataType = Nominal, legend = Legend(orient = "left", title = "Place of Origin", titleColor = "red")),
    Vegas("Plot to show Binning options")
      .withURL(Movies)
      .mark(Bar)
      .encodeX("IMDB_Rating", Quantitative, bin = Bin(step = 2.0, maxbins = 3.0))
      .encodeY(field = "*", Quantitative, aggregate = AggOps.Count),
    Vegas("Plot to show Binning options")
      .withURL(Movies)
      .mark(Bar)
      .encodeX("Worldwide_Gross", Quant, bin = Bin(maxbins = 20.0), sortOrder = SortOrder.Desc)
      .encodeY(field = "*", Quant, aggregate = AggOps.Count),
    Vegas("Plot to show usage of encodeText")
      .withURL(Cars)
      .addTransform("OriginInitial", "datum.Origin[0]")
      .mark(Text)
      .encodeX("Horsepower", Quantitative)
      .encodeY("Miles_per_Gallon", Quantitative)
      .encodeColor(field = "Origin", dataType = Nominal)
      .encodeText(field = "OriginInitial", dataType = Nominal)
  )

  val userChoice = StdIn.readLine(s"Insert a number from 1 to ${examples.length}> ")
  val index = userChoice.toInt

  require(1 <= index && index <= examples.length, s"$index is out of range.")

  examples(index - 1).show
}

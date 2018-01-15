package codegen

import codegen.Config._
import slick.SlickException

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Generator {
  def main(args: Array[String]): Unit = {
    Await.ready(
      codegen.map(_.writeToFile(
        "slick.jdbc.PostgresProfile",
        args(0),
        "com.anymessenger.dbmodel"
      )),
      5.minutes
    )
  }

  val db: profile.backend.DatabaseDef = profile.api.Database.forURL(url, user, pswd, driver = jdbcDriver)

  //  val included = Seq("user", "message")
  val codegen = db.run {
    profile.defaultTables
      //      .map(_.filter(t => included contains t.name.name))
      .flatMap(profile.createModelBuilder(_, false).buildModel)
  }.map { model =>
    new slick.codegen.SourceCodeGenerator(model) {

      //make lazy val ddl vertical for better diffs
      override def code: String = {
        val start = "lazy val schema: profile.SchemaDescription = Array("
        val end = ").reduceLeft(_ ++ _)"

        super.code.lines.map(line => {
          if (line startsWith start) {
            s"$start\n  ${line.substring(start.length, line.length - end.length).replaceAll(", ", ",\n  ")}\n$end\n"
          } else {
            line
          }
        }).mkString("\n")
      }

      override def Table = new Table(_) {
        table =>
        //override def autoIncLastAsOption = true
        //override def mappingEnabled: Boolean = true
        override def TableClass: TableClass = new TableClassDef {
          override def code: String = {
            val prns = parents.map(" with " + _).mkString("")
            val args = Seq("\"" + model.name.table + "\"")

            s"""class $name(_tableTag: Tag) extends Table[$elementType](_tableTag, ${args.mkString(", ")})$prns {
               |  ${indent(body.map(_.mkString("\n")).mkString("\n\n"))}
               |}
             """.stripMargin.trim()
          }
        }

        override def ForeignKey = new ForeignKey(_) {
          foreignKey =>
          override def dbName = "postgres" //suppress regeneration of random SYS_C0019264 like foreign key names that produce gigantic git diffs
        }

        //      override def entityName =
        //        dbTableName => dbTableName.toLowerCase.toCamelCase

        //      override def tableName =
        //        dbTableName => dbTableName.toLowerCase.toCamelCase

        override def Column = new Column(_) {
          // override def rawType: String =
          //   if (model.name == "id") "Option[Long]" else super.rawType

          // override def rawType: String = model.tpe match {
          //   case "java.sql.Date" => "java.com.anymessenger.util.Date"
          //   case "java.sql.Timestamp" => "java.com.anymessenger.util.Date"
          //   // currently, all types that's not built-in support were mapped to `String`
          //   case "String" => model.options.find(_.isInstanceOf[ColumnOption.SqlType]).map(_.asInstanceOf[ColumnOption.SqlType].typeName).map({
          //     case "hstore" => "Map[String, String]"
          //     case "int8[]" => "List[Long]"
          //     case _ => "String"
          //   }).getOrElse("String")
          //   case _ => super.rawType
          // }

          override def defaultCode: Any => String = {
            case Some(v) => s"Some(${defaultCode(v)})"
            case None => s"None"
            case s: String => "\"" + s + "\""
            case v: Byte => s"$v"
            case v: Int => s"$v"
            case v: Double => s"$v"
            case v: Boolean => s"$v"
            case v: Short => s"$v"
            case v: Long => s"${v}L"
            case v: Float => s"${v}F"
            case v: Char => s"'$v'"
            case v: BigDecimal => s"new scala.math.BigDecimal(new java.math.BigDecimal($v))"
            case v => throw new SlickException(s"Dont' know how to generate code for default value $v of ${v.getClass}. Override def defaultCode to render the value.")
          }

          //override def rawName = (table.model.name.table, this.model.name) match {
          //  case (_, name) => name.toCamelCase
          //}
        }
      }
    }
  }
}

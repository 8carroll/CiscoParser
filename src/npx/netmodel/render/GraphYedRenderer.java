package npx.netmodel.render;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import java.util.Iterator;
import java.util.List;
import npx.netmodel.config.PassiveConfig;

/**
 * YedFileWriter writes a Graph for the yEd Graph Editor to a GraphML
 * OutputStream.
 *
 * original by Benny Neugebauer (http://www.bennyn.de)
 * changes by Luis Dias costa (lcosta@cryptotactics.com)
 * 
 */
public class GraphYedRenderer extends Renderer {

    private String xml = null;
    private final Graph graph;

    public GraphYedRenderer(List<PassiveConfig> configList) {
        super(configList);

        GraphRenderer gr = new GraphRenderer(configList);
        this.graph = gr.getResults();
    }

    private String getGraphMLHeader() {
        String header = "<?xml version=\"1.0\" ?>";
        header += "\r\n<graphml\r\n  xmlns=\"http://graphml.graphdrawing.org/xmlns\"\r\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n  xmlns:y=\"http://www.yworks.com/xml/graphml\"\r\n  xmlns:yed=\"http://www.yworks.com/xml/yed/3\"\r\n  xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\r\n  http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\"\r\n>";


        header += "\r\n <key for=\"graphml\" id=\"d0\" yfiles.type=\"resources\"/>";
        header += "\r\n  <key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>";
        header += "\r\n  <key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>";
        header += "\r\n  <key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>";
        header += "\r\n  <key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d4\"/>";
        header += "\r\n  <key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>";
        header += "\r\n  <key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>";
        header += "\r\n  <key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d7\"/>";
        header += "\r\n  <key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d8\"/>";
        header += "\r\n  <key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>";
        header += "\r\n  <key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>";
        header += "\r\n  <graph edgedefault=\"undirected\" id=\"G\">";



        return header;
    }

    private String getNode(String id) {
        String node = "\r\n    <node id=\"" + id + "\">";
        node += "\r\n      <data key=\"d5\"/>";
        node += "\r\n      <data key=\"d6\">";
        node += "\r\n        <y:ShapeNode>";
        node += "\r\n          <y:NodeLabel>" + id + "</y:NodeLabel>";
        node += "\r\n          <y:Shape type=\"rectangle\"/>";
        node += "\r\n        </y:ShapeNode>";
        node += "\r\n      </data>";
        node += "\r\n    </node>";
        return node;
    }

    private String getHostNode(String id) {
        String node = "\r\n    <node id=\"" + id + "\">";
        node += "\r\n    <data key=\"d5\"/>";
        node += "\r\n    <data key=\"d6\">";
        node += "\r\n        <y:ImageNode>";
        node += "\r\n          <y:Geometry height=\"75.0\" width=\"75.0\"/>";
        node += "\r\n          <y:Fill color=\"#CCCCFF\" transparent=\"false\"/>";
        node += "\r\n          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>";
        node += "\r\n          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" modelName=\"sandwich\" modelPosition=\"s\" textColor=\"#000000\" visible=\"true\" width=\"10.673828125\" >";
        node += id + "</y:NodeLabel>";
        node += "\r\n          <y:Image alphaImage=\"true\" refid=\"1\"/>";
        node += "\r\n        </y:ImageNode>";
        node += "\r\n      </data>";
        node += "\r\n    </node>";

        return node;


    }

    private String getNetworkNode(String id) {
        String node = "\r\n    <node id=\"" + id + "\">";
        node += "\r\n<data key=\"d4\"/>";
        node += "\r\n<data key=\"d5\"/>";
        node += "\r\n<data key=\"d6\">";
        node += "\r\n<y:ShapeNode>";
        node += "\r\n<y:Geometry height=\"18.0\" width=\"125.0\" x=\"507.5\" y=\"311.0\"/>";
        node += "\r\n<y:Fill color=\"#FFCC00\" transparent=\"false\"/>";
        node += "\r\n<y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>";
        node += "\r\n<y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" modelName=\"custom\" textColor=\"#000000\" visible=\"true\" width=\"10.673828125\" x=\"57.1630859375\" y=\"-0.3505859375\">";
        node += id + "<y:LabelModel>";
        node += "\r\n  <y:SmartNodeLabelModel distance=\"4.0\"/>";
        node += "\r\n</y:LabelModel>";
        node += "\r\n<y:ModelParameter>";
        node += "\r\n<y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/>";
        node += "\r\n</y:ModelParameter>";
        node += "\r\n </y:NodeLabel>";
        node += "\r\n <y:Shape type=\"rectangle\"/>";
        node += "\r\n </y:ShapeNode>";
        node += "\r\n</data>";
        node += "\r\n</node>";

        return node;
    }

    private String getEdge(Edge edge) {
        Vertex source = edge.getOutVertex();
        Vertex target = edge.getInVertex();
        String edgeId = (String) edge.getId();
        String sourceId = (String) source.getId();
        String targetId = (String) target.getId();
        String label = edge.getLabel();

        String edgeXml = "\r\n    <edge id=\"" + edgeId + "\" source=\"" + sourceId + "\" target=\"" + targetId + "\" label=\"" + label + "\">";
        edgeXml += "\r\n    </edge>";

        return edgeXml;
    }

    private void createGraphXml() {
        xml = getGraphMLHeader();

        // use the generic graph renderer

        // Create nodes
        Iterable<Vertex> vertices = graph.getVertices();
        Iterator<Vertex> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()) {
            Vertex vertex = verticesIterator.next();
            String id = (String) vertex.getId();

            int type = (Integer) vertex.getProperty("type"); // 0==host 1==network
            String node;
            if (type == 0) {
                node = getHostNode(id);
            } else if (type == 1) {
                node = getNetworkNode(id);
            } else {
                node = getNode(id);
            }
            xml += node;
        }
        // Create edges
        Iterable<Edge> edges = graph.getEdges();
        Iterator<Edge> edgesIterator = edges.iterator();
        while (edgesIterator.hasNext()) {
            Edge edge = edgesIterator.next();
            String edgeXml = getEdge(edge);
            xml += edgeXml;
        }

        xml += getGraphMLFooter();
    }

    @Override
    public String getResults() {
        createGraphXml();

        return xml;
    }

    private String getGraphMLFooter() {
        String footer = "\r\n  </graph>";

        footer += "\r\n        <data key=\"d0\">";
        footer += "\r\n    <y:Resources>";
        footer += "\r\n      <y:Resource id=\"1\" type=\"java.awt.image.BufferedImage\">iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAqYElEQVR42u2dCVSUWZbnyTQCRPYd&#13;";
        footer += "\r\nERUB2QkIEBVRdmQTFNx3cWcRZN83FfctEbd0Q9wXBGQX9z1TzUyzlp4+c6bO9FT39HSfPj11prvr&#13;";
        footer += "\r\n1HRN1Z173xdfxBdBAAFiplZ975x/BokQy/v/3r33ve+9Dz09sYlNbGITm9jEJjaxiU1sYhOb2MQm&#13;";
        footer += "\r\nNrGJTWxiE5vYxCY2sYlNbGITm9jEJjaxiU1sYhOb2MQmNrGJTWxiE5vYxCa2n6ktPqtnmXJO6jf/&#13;";
        footer += "\r\nwpiklEZp5oLzkn0p58ecWXBeeiXlnOTO/HOSnvlnJU9Qb+afkfwy+cyY3ySfkfxj8teS3yWflvxn&#13;";
        footer += "\r\n0mnJ/0s6Jfm3pJOSf046Kf3v805K/gb1LvGE5Hniccm9xAZJW2KD9FpCw5jzCcckR+PrJfmJx8Ys&#13;";
        footer += "\r\njq+Xzkw+pudQU6P3pejCxzT4pt6YlCZ9n9Qm6erUi5LK1IvSM6kX9Xvw8deof09plADTBU4IAKdz&#13;";
        footer += "\r\nnNB4Tmc4ofGcTnNKIp1S6KQE5pFOKHRcAomkBoWOSSCBVK/QVxKI/0rynwlHx/wm/qjkScJh6SV8&#13;";
        footer += "\r\nrIs9LN0af0QSMveAnpHo4DBa+CM9yfwL+r5o9jo09xjqJer3KBhIgwGQcl4fFp43hEWNRrCk0RSW&#13;";
        footer += "\r\nN5mjrGDVJRtYc9ke1l5xwEcHWI1fr7xoA8svWMGS8+aw6KwppJ42ggWnDCHphL7S/ESh+SoAAE3n&#13;";
        footer += "\r\ndEQCcaTDCh2S/Cn2kORXsQelTaicuIOS2SIUgrbugt7Y1MYxiWhkPerVUGaTFjWNheVXzGDNdRvY&#13;";
        footer += "\r\ncNMRtjQ7w7Y2L8ht94eirhlQ3hsK1X1RUHM/ZmD1DaF7/VXVGwVlXaFQ0D4DcprlkH7DCzZcdoHV&#13;";
        footer += "\r\njY6w9IwNLDhuCglHDYTmQyzpoEpzDzD9ae5+ya9jDkibovdJ0xAI27+ukH5Bzx6N3IjhvA0f/2Mg&#13;";
        footer += "\r\noxc2GcDKq5aw4dZENNgTCjuDoLIvHGoexECtQDXadD9m1AHgVT2IqnpjoKwzHHLvBMGWq16w6txE&#13;";
        footer += "\r\nSGmwgPhD+hB7QAHAfoX2SSCGtBeB2Cd5OXePpDRir773X6TpKZekfix/N0m/RXP/rM3sNddsYcsd&#13;";
        footer += "\r\nF9h+1w9Ke2ZB9f0oNaMHktB0AqSsNwSKuqdDYdc0KOgKgLxOf8jtlMH2Dl/I6fCG7HZP2HbXHTLb&#13;";
        footer += "\r\n3CCjzZWJvs7C722764n/7g057b6wvV0GeR3+kN8RAAWd0xDA6VDaHQIVPeFQ3TsICL39VdEVDQWt&#13;";
        footer += "\r\nsyDjmh+sO+8CCxtsIG6/Pg8Apz0SiCbtHvObqD2SozF1kqjwGj3JZ53PFzbqL0eD32gb4cuvmMPm&#13;";
        footer += "\r\n285Q0BHIQnbtwxiVtBhdfT8SSu8FQ2FPABrqC9kd7pBx1xk2tTrC+hY7WNtsCWuaLTjdHkK3LGD1&#13;";
        footer += "\r\nQLo5sFaRbuDXNyxh3S072NiMKeiOM2S2ukP2XV/Iaw+A4s5gqOyJVAOgSqgeThWdUZB9cxqsOesM&#13;";
        footer += "\r\nyYfNBAAoVCeBqDrJb6N2SYoQBPPPJ8Sf1jPDoqwATf4fQsMXXxoHaTccILvNFyowT5OpOx6qJASg&#13;";
        footer += "\r\noi8UinqmwfZOL0hvnwIbWtHgOxYqNXNaM5BGar6OAAyq65zW3bTD+mQKbGv1QjCmQSnWD7z5mqrs&#13;";
        footer += "\r\njoGillDY3OgLS+odIHavIZnPaRfTv0fuGnMscqeBy6drfNPYKSkXpUfR7H8TGr/yqhXkoOlVfRGw&#13;";
        footer += "\r\n41EMJzXjo6Hk3kzI6fKEzXcdYV2rlbrZ2qQFgE1tE2BbpztGiECouB+KrzMXqh9EQnnfbCjpnQG5&#13;";
        footer += "\r\nXRjWO72htDcY9j5ZAI3fFcD1H6vh2vtKuPq+Aq68L4fLP5TB5e9Loem7YlQRXPyuEBrfFcCFd3lw&#13;";
        footer += "\r\n/m0unHubA4efr8QUFQLrbzsMaL6mViq06roVrL/lCJktWNN0zMRIEa0OQzensrZI2NzkC8kHrTgA&#13;";
        footer += "\r\ndkogkrRD8idUS2SNJPSTmqenNkpK0ez/qxztTYaw8eZkKOqcifk5WmW8QhX350B+twwy2p0gjQxv&#13;";
        footer += "\r\nsVBJR/NJG1sdoPheEOx/lgJHX67USfufpkIxApHWbA8F3YFw4ptNcPuXdSr9QrtuaejG+1rY9yQV&#13;";
        footer += "\r\nNjRPGBSAlUJdw8hwwx5y7soYDKRNt50gp1UGJZ1zlACQKCpUdsXA9hvBsKx+MsTUjSUAONVKIKJW&#13;";
        footer += "\r\nehNTg/XPa/5lfS9FYceMX3LJGDLveGKID2OhXN302ZDd5Q4b2+xgHRot1HAByMaRvvNxrM6mK/Vi&#13;";
        footer += "\r\nJRxR6NDzZVB2LwQ23BkPhd3TWEQYyHxtAPC6/r6GFaBUF2gDYFPzZCjungX7Hy+Gc99uh8a3RZDR&#13;";
        footer += "\r\n4s5gUNNV/GzX7SCj2R1KOmZzAAhAKGoOh3WnvCC2zgjNl3CqkfxTRM2Y1J9n1DdJitD0P7Aq/qI+&#13;";
        footer += "\r\nG/GV98LUTK99GAWFvXLY0k6h3YJTi8WIAcjqnAq7nyTCV8M1XgsApMPPlzPzKfevvW0Nux4mwI0f&#13;";
        footer += "\r\na4cFAK/T32RCestULAjdoOpeJBx9vhrTSAnc+rFOqZOv0jEFTOhvvgIAXitQaTcdYXubHCq6o5QQ&#13;";
        footer += "\r\nkIpbImA5RoTIHVICgCm8WnotqlTP6qeZ0jXp2SoWbbgcf8USK/kgbsQ/5oyvfDCHjfb1bdYq4wcA&#13;";
        footer += "\r\nYK0OAKxvtYWaR5HMeF4fCsCOh3GwqcWxXwG4tdUZTrzeNGwAhEZr077Hi1jI12q+FgB4rbpmDem3&#13;";
        footer += "\r\nKSrMUUJA2nZ5BiTutoCIagIAVSX5X2GVkjkff+XuovQ1Z74+pGO4ouJu52PO/JpHUZDT7Yl53bK/&#13;";
        footer += "\r\n8boAMET439Q6HnI6PaC0bya+ZhwcerFk2AAcfLaErQsMVf2X9MzEgrBMN/MHAeDG+x1QjilxQOM1&#13;";
        footer += "\r\nzNcEgOmKBRtoGc2YXruilBCUtkbBqgZ3igAEAOl/R1QYuH8c90HvC6zyb/AjPwsrWargyXxScd80&#13;";
        footer += "\r\n2NhuN7DxHxj+B6r+N7dhqOzygbK+EKh7nIije/mA5lfdj2TF32DTvy0tTlDcMwP2PJ4PZ95kfRAA&#13;";
        footer += "\r\nl74rg213vQc3X0cAeK25Zgd5bdPUosGGs948ABBWOea/BtfoWY5+6G/U38Wbn948lU3hyPiahxGQ&#13;";
        footer += "\r\n0TkZ1rVZDG7+CML/uhYrFv43tNrjNG/84PN/xTx/bbMVbGmbDHldfmw6uOdJMux7ugC2tbv1M31z&#13;";
        footer += "\r\n6yQo6gmC3Y+TWdi/+kPFiPK/NvPPfJMFG25PVFb/ugKwYggAeFHNVdYZoYRgzUl3BQASCK2UPA7c&#13;";
        footer += "\r\nrCcdxbyvn8qbv+nWFDa1I/NrMS9v6ZjAmS8AYONdeyi5Pw0OvkyFhm/XwIk3aXDy7UY4/XYznHmb&#13;";
        footer += "\r\nDmffZcH577Kh8ftcaPqhAC69L4YrP5bBtV9UwI1fVMOtX+6A5l/V9dPep8lDAqDrAtCGOw6QhVBk&#13;";
        footer += "\r\nd3jB9k5ftmRc0BUIRd1BGP6Doax3NlRgUVvZFwnV96Oh9kEsFomJsPtRMkaHBTgFXAgHni6Bw89W&#13;";
        footer += "\r\nsIKv/sVaaHi5Hk682oT5fiHOCqzVpn8jBuDKwEq7PgHKOyMZABUdMbD8qykMgLAK0pgTowZAaqP0&#13;";
        footer += "\r\nHb+oQ0uyLOc/iob0jkmQxps/QATI6nKFmscRcOz1ajS5BJp/Xaddv9JN9a9Ww7o71p/k6p/W+f8o&#13;";
        footer += "\r\nhn9tWn99ElR0RjMIytpjIHGvlQIAyR9DqvUcPnz0X5QE86OfLrnyOT+ry5mZnzaI+drCf0aHE+x9&#13;";
        footer += "\r\nloSm7xoRALdRp9+kw0YsCEdl7f9DAbg+MgDW3rBTXPzyh9Ku2VDSFQKrr9sOD4DLnDbedFamgoxL&#13;";
        footer += "\r\nATwAMKdcUvvBACxsklTxq3s1D6KY+eX3g9XN1xEAWg84/HIxmrhzxBHgtkKN3+dD+t0pHx+ADxn9&#13;";
        footer += "\r\n14VG+0FJdwjU9sWxKeHhpyv7ae+jhZDR4qHz6OcBWI4quBusTAVRNYYMgNAKycsPD/9N0osEAL0g&#13;";
        footer += "\r\nzfEJgNwer2EDUPMoAm7+smbY4f/y+xJoeL0Wmr4vUAOAdPXHcsjp8vroV/90AWDtTVvYSka3c0bX&#13;";
        footer += "\r\n3I/FOkG70QPpkEIVPZFs/j8cADJueymjQNJ+Kwgtl5D+6cMjwEX9dgJg9TVbZfjP7p46LACOvFo6&#13;";
        footer += "\r\ncO4fAIDrv6hiy73rW2yU1T9d9qUpX/XDSDj8Yhmce7cNrv1YCcW903/W/L/6hhXbM3Dw6VIsCleq&#13;";
        footer += "\r\n9HTliAAg1WCkoJprKACWK7Tl1lQlAKmH7XkA/vjhNcAFbq1/+WUzttpHAOT3+uqc/2lRaNDi79ea&#13;";
        footer += "\r\nIX4nHHyxEKd9Djpd/qVpX2b7VPb4sQFYd8t+0Is/dAmYNo0cerp82AAc0qKK7kidRj8pq9lXCUDC&#13;";
        footer += "\r\nHnMegH/+YADmn5P+Db9rh6ZFBED1w3BY32apc/jP6nRBCEqHBKDhm7VYJDoPePVvNKZ/ugIgND+n&#13;";
        footer += "\r\n3YdN9+gKYEnPrCHzf9otB6jojfgwAJ6shLr783UCYMVlSyjtCOdWB9siIbxSnwcAPhiApK+lj5QL&#13;";
        footer += "\r\nQHemKtNA4T1/3QBQVP+Znc7aIUDjz7zNhNxubxbuN7bZM9HCD0WBTa0OsLltgkKOqIlMW+5OQk1m&#13;";
        footer += "\r\niz5b25yUGq3Rv755PNupdP5tntqiT+W9CJ0LwE23J2MonztsAHY/XAD57UGw7vp4nQDIvuOvHP3r&#13;";
        footer += "\r\nzngozadC8IMBSD6l37PgPH+51whD3GwlBHm9PsNa/eMgUKWDqz+WQWnfDDbSC3r84cjLZfDVq5Xq&#13;";
        footer += "\r\nGsbFH1rx+1AAcjv94OiL1XDjxx1aV/1q7s8d9vSPBk7VvSgUztd7I1l0KO8Oh7LuUDYFLOoMhhyc&#13;";
        footer += "\r\nDma2eDHpOv3jQr+P0vyCm2EQXT1OBUDlKAGQ/DUC0MhBQG+AUsEOxQWgnG6PYS3/ZnRMYSt/Te8L&#13;";
        footer += "\r\nIb3DiY10Kvb6GS8wX1cA6NrESADYeGcC1D6Yy3b/DLX8SzuDaOXvyLNVTFzBt4KJcj/p4NNlcODJ&#13;";
        footer += "\r\nUoWWMNF+AJr+0Soh0yNuhlD/PA2Ov9wMex6m4KifhsBY6Vz9p9/2UF0mbo2E+F0W3PRPMfpHDYCk&#13;";
        footer += "\r\n01JIPqPa5rX6qg1U9UUqL//SNf8NbTY6r/+T8TseR7ORv/fZfO3mjwCA0nszdQZgzU1LyO8KgPqX&#13;";
        footer += "\r\na9HYncNb+9fh8u+ges/p/Ld5UHNvLqy/6Tis1b/VV2xge4tcaX7Z3WhI3mPTz/xRBQBrAVhwTgXB&#13;";
        footer += "\r\n2uv2LB/ymz9qcGqW3eU2rKt/dLFnz9OkUQNge6ePTuZn3HXF0Z4/7N0/owHA1e+r4MDjJZCFoX4k&#13;";
        footer += "\r\ny79bb7tBeUek0vySlihYcGC8cvUvlB7LpaMPwPyz+pD+zAJTgb4SglVXraGwYwbbl8+DUH5/lmoH&#13;";
        footer += "\r\nkA5X/+hKH13fr3uSgDXA8hHnf5JaEThE+N+ARd6OB7Fw6fuSUd/8oU0nMMwXd81iJ5pGcvUv7YYj&#13;";
        footer += "\r\nFLXPUhpf0RkDWZdmQsIuK8EFIAnMycMaoET/4wBQ8IMDbPvWGhZfNlA71LH5ljNGg3C17WBlfcGQ&#13;";
        footer += "\r\n1eEKaTjKh3P5N7PDBfJ6fKGoN5CBQRtIaeGHNoDsfjIPdmPE2Pt0PmoB7H+aAvufpcKBZ4vg4PPF&#13;";
        footer += "\r\nsK7Zetj5n1IBHSQ5/U360Ob/uIvVCY3vCuHiuyK25evSd6VM597kwsnX6VD/Yh0cfLKMVfI1fbFQ&#13;";
        footer += "\r\n3hPGZgMjufpHC0FbbrlCoWKZV7kRpCUSln/lDBFV+irzcdTPyTOCOTnGHw+Aih/dofQHV8j/zgFW&#13;";
        footer += "\r\nt5lASqMqJSy9bAK5WMnSiR7h1u+aB5FsBzC7nj/C3b8/1d5/OiF06NkyNt/XBgCZr+vu3w+5+kfT&#13;";
        footer += "\r\nv+wWmfJSrzDXb70gh7k7jZXX/tn1/2IDCN1uDGG5Jvho8nEBKH/vAeU/eEDxdy6Q82o8LLk6Vu3E&#13;";
        footer += "\r\nDxWIOa2+bGOkcP8/iU72ZHd5wqa7DiPa/fNTXf1bf3s8m+7R+QAhAF9/mzXiq39DAbDuhgNk3PHs&#13;";
        footer += "\r\nN9q5ER8Fm87KYN4ea37/HwdAORqdbwTheSYQlmeKAJh+XADKBQCUfY/6zgOK3rrA1vu2mAr01UCg&#13;";
        footer += "\r\nNQNKDbQ0yo53aZz+qbofzo55Zba7wPoW209i9U/z6h9t9S7sCsLQvpUB0PBqw4eNfgEAdBZy820X&#13;";
        footer += "\r\n2N4WAGVd4Wo7f3nl35oDKxpccMSPE27+hPBKKYSX4PcKTCGclM+Z/9EjgAoAdwUAmBLeod66Q/Gb&#13;";
        footer += "\r\nqbC5x54dAVM/B6iPhY8dSw8UFWoGOORZfi+End4hIGj1b12z5ccBYIQXfzJa3aC4e+aIAFh1zRLS&#13;";
        footer += "\r\nbjrAFjQ8u8UXijtCVIc/NPb/l2B+39okh9RDtnT4Q7n1mwCgnB9ZZgRRxWYQUWQKEYUKAPI4838y&#13;";
        footer += "\r\nAEq+d4Hcd+Oh+J2LEoCSN25Q8i120jdukP1oEqy4Yab1bD9taqSjUdwl0+iBT/3iv5Xfmw0F3QGQ&#13;";
        footer += "\r\n3eHJKnyKFJ/67p+1N2zZaR/6jLl3A6Ckc7bqCFh3TL/TPxU00m+HwPpznpByyA6idxmw0z+Cwx8Q&#13;";
        footer += "\r\nWT0WoitMILrUHKJKzNQACMtHw3Ox8PupAch5i7n+jQ1sf2MPBW8mswjAA1D0eioUogpeuEBaqw1G&#13;";
        footer += "\r\nBUOt9wCgwnHjrUmQ2y5nawq6nP2v7ouGinuhbN8e7eGjk8Lb2j0gvc0ZNrdMhPXNdgiB5agDwE4E&#13;";
        footer += "\r\n37SDjbcnsptSZLZ4sDOP+e2BbCmXDn9WdkcPeACUB4Dbzh0BGVflsOLEJIjfZyw8+6c8+hW5Qx+i&#13;";
        footer += "\r\nq40gptIMYsrNIbrMXAlAZJEZhBcas/wfipV/aK7RzwdA9reob2xg22sbyH3tAAWvpygBKHyFELwk&#13;";
        footer += "\r\nEKZC1oNJOEItYdEAMNDpIrrknHadiiE3dtS6rGcOd0OHEd78ga29I1gVveHsziGlPbMx8szC+fhM&#13;";
        footer += "\r\nKOqazq7hk+jrIvwezdPpWget0Zf3hENFTwRU9Uar7geg5ey/tqPfwlO/xW2hsO16AKy/4AZLGhwg&#13;";
        footer += "\r\ncb8pxNRJhSd/lQBE7zCAmFojiK02hbmV5kwxFeZKAKJKcNQXmXDmFxh9agBYw7ZX1pD1Eh9fYM5/&#13;";
        footer += "\r\nNREKXrkqAch/jnrmCnlPXSHn0WTY2G4Ly64ZD3nvn0WNY3FmYQ2bbjnBtlYfyMOwWtwVzG7UUIPR&#13;";
        footer += "\r\nYFh3/xjhnT8Gu/kDGU2XYukGENm3AmDrVR9Yc24KLGywhrj9Y9nNH6I1zv7zR7+jd0lh7q6xELvT&#13;";
        footer += "\r\nBOJ3WEB8jSWabwGxVeZKAKIrMOSXmUBEiTFEFOP0r8j40wcg64U1ZJKe4/eeO0DucycEwJUDgEGA&#13;";
        footer += "\r\nNcQT1GN8nodTYEs3Ny1ccmUcKxoHvPGT4uZP8xV3/1pwVgqLzxvDiiYrdsOnjTecIOO2B5tDb2+V&#13;";
        footer += "\r\nIyzTIP9uEDuKXdQ5C0q6ZrP7/NCVOLoqxxtLX9P3SjtDsUCbzVbcCtpmQl5rEGy/Mw2ym+WQdVMG&#13;";
        footer += "\r\nW655QFrTFFh5zgEWnbSC5HpjiDsoHejWLxp3/sCvd+tD7G5DiNttDAl1ZpBYZwkJu1A7LSG+1lIN&#13;";
        footer += "\r\ngJgKU4gqN8GizxgiS411BmDOpwZAxjPUU9QT/JknDpDzZDKa76wEYPsjZ9j+0BlBcIbsB86wrQ9N&#13;";
        footer += "\r\n7HVkp4zW3DaHpVcM2fLzsG79Jrjt2whv/TbYnb/Ub/40wL1/5u6VQtw+Q0jAHJ+4zxyS9lmirGDe&#13;";
        footer += "\r\nXtQeK0jcbaUCYIclxNWi8dWY7yux2KvAuqCc018MAOlPUI+tYesj1EMryHhoB1kPHSH7oRMDIJsH&#13;";
        footer += "\r\n4D5B4AxZ96ZAVi9W0z0IRNck2NzuwE4JUR2xEmcYS68Yw6KLCMd5AxYJBgVAYP48DfMTNcxPEJo/&#13;";
        footer += "\r\nCABxh6Ts5k/xh8ZC4mEjmHfIhN3qJfmQBcw/bAULDtnAgoM2MP+ANczfbw3JaL4aAMx8c4jdYQZz&#13;";
        footer += "\r\na0wgphpVheZXGo8YgNDPCYAtD1D3rWBzHz7es4GtffaQ0ecIWX2TEYApagBkdhMEqE4nSO+YzLS1&#13;";
        footer += "\r\nnXYDTWL3CKIz/mlY/a+9aYNTMUtYccUcll1CSC6awpJGE1h0wRgWnjeChWfHQcoZ1NeGsOC0Icw/&#13;";
        footer += "\r\nORaSUfNPGCo0jmnBcSNOJ0zw0QRSjptCSoMZpBwzh9QGK1iE+X1Rgy0sOmaDsoWF9baQ+pUNpB61&#13;";
        footer += "\r\ngZQjqMM2/QBI2muJ5ptDwm4ziN9lyvJ+7A4TmFtr8kkD8IU6AG4IgPuoA7D5nhVsIvVawcYeVDd+&#13;";
        footer += "\r\n3W2LtYE9bO2awCKAVgDuTobNbZPZGb9NOAXcdGcibGyeyKZqdHu5DTdxWnjDkWndddQ1B8XNIcfD&#13;";
        footer += "\r\n6kuoJntYdRHVOB5WXhgPqy7QowOsPG8Py8+hztrDsjOor+1h6SnUSTtYcgJ13A4WN6CO2SoAsFED&#13;";
        footer += "\r\nYMFhHPEHMeTvt2DhP2GPKcTvNmGKq0PtMvkIABgPBMAXIzaflHRav/fnAGBDF6rTEtZ3oNpRd/H/&#13;";
        footer += "\r\n79rAhjaci98dDxtpv2CbI0aAiToBkMYAmABrr6KuTGB3CF1zyQEhQF10QAhQZD5BoAnA6f4AUARY&#13;";
        footer += "\r\nWG8NKfWWsOAohv0j5hj+zWDeQVOYd8CUTfcS95lCwl7TTwGAL0ZkPurLpFOfBgBpd1G0E7mV09oW&#13;";
        footer += "\r\n1B26NZylYhUQZwK3rFG2bMGGTuTQLVfWXrNnWnNtPKy5io9X7AURAEf9RVSjPay4gDpvB8vO4fT0&#13;";
        footer += "\r\nrA0sPWMNS762hsWnrWDxKStYeMISUo9bsDSwoN4M5n+FOmoKyaQjppB0GHXI9FME4MvhQqA0HyWZ&#13;";
        footer += "\r\n95kAwK/y8cu1/IUX/to62z9/CWuDJtRFnF00oi6Ys3sDLzlnDovPos6YwyLS12aw8DTqlBmknkSd&#13;";
        footer += "\r\nwPx/3IzVAQuOmX3iAPTbDyAZDgS88cx8vcBAadIp6T0RgM8YAPRQAMGXg0GgNvL1vL31nZycxs47&#13;";
        footer += "\r\nKe3rD4CzCMBnAgB5SF4OFQm0mm8nkxkhAPdFAD5fAMhDXSDgzR+jpxcoZebbyYxcXV1N552QPlAC&#13;";
        footer += "\r\n8F4E4NMGwLgfAOQheckg0GPpYIxmKhCYryfFXzBwCAwcR7/o7e1tmXRC+lAE4PMFgDwkLxWeGpDH&#13;";
        footer += "\r\nmhAoQz/+sL6jo6Ohu7u7ySRfXwsXmcw24bjBYxGAzxcA8pC8JE/JW+/+qUA1+ilM2Hh7Gzv5+5u7&#13;";
        footer += "\r\nuspt3OVyh8QGgydCAMpEAD4rAMhD8pI8Zd6yVKAWBRSFH4V+BwcW+j08PKw8PT3He3rKJyceM3gq&#13;";
        footer += "\r\nAvD5AkAekpfkKUsF6LEelwr4KKAx+p38zSlsuMpkjm6+vs6J9WOfcWcD9aHku6kMgLIf3CDv3QQR&#13;";
        footer += "\r\ngE8VgFIVAOQheUmekrdaogD9R5X7qWhA2RM5Hn5+bvGHjK6zs4GoFdfNoZR2BKNKv3eD3LfjRQA+&#13;";
        footer += "\r\nAQDC8oxVABSNVR0YKZP8ljwkL8lT8pavBchzHgBW/FFocHZ2NnNzC7R28/ObQOT4+gZ6BEZ4zcA6&#13;";
        footer += "\r\n4G95CNLarFkEIABKv8NI8GaCCMDPBoAxJx6AAkOl+WEVkt9PX2q/mDwkL5mn6C15TF4LikE9CU0P&#13;";
        footer += "\r\n7OzsaK7Iij8KGT4+gS7ecrmXl980+YylTvOTTur/jkFwSgrrmm2g5N1UBkDJOze2K1gE4KcFgKnA&#13;";
        footer += "\r\nhG0LZwAUjoMwwXGxmRutysg78pC8JE9ZMYgek9euqjqAB4AtFpi7uMhsXXx8Jnp5Bbh6+fl5e8oC&#13;";
        footer += "\r\nAzxlATNDM+0zk07p/wcBQFqBHVz07VQGQMlbNyj8dgrkvLYXAfjoAJiwMwHhhSYKAND8YkPOfAUA&#13;";
        footer += "\r\nIdtMLpJn5B15SF6Sp+QtB4Bs+AB4yQLmzFrnuGnecf1/4SFYfNEYcp5MYQDQuYDib10h7/Uk2PbC&#13;";
        footer += "\r\nVgTgIwAQWWICkcWmAgAw/JcaqMyvkvx5VqbZafJqBAAMnAK8ZIEzvP0CZ/v4TwufkeK2DGcGv+Eh&#13;";
        footer += "\r\nSDplAGl37KDwlSsDoOgbVyh87QI5LyeIAIwSALQtPKrUlJ0LUAJQMo47I6ga+X+ctdmqjjwir8gz&#13;";
        footer += "\r\nnVPAYEWgt7fcz9vfP8jHP2iWrzwo1FsWEDktwjs5bq9RHwEw7ySnheeMIKN3IgOg6DVB4AoFr5wh&#13;";
        footer += "\r\n54UjZD61FQEYAQDRFaYQXY4qM1UCEIFRILzcgDsoqjgsGloh/dcZKyfkkzfkEXlFnpF3uhSBg04D&#13;";
        footer += "\r\nPWXTfPg0wEcBeiEff/+Y0HSH4sRjBn/PQ0Bid67qc1IA4Ar5L10g/wVGhGeTIPOJvQjAEADQtvC5&#13;";
        footer += "\r\nlabckbAKMwTATAlAZBmO+iqpyvxquiOIUa9fmFsK5wk3+vnwT97pMg0cdCHIxyfAk0jy8ZFPk2lA&#13;";
        footer += "\r\n4CuXR/sF+82LqTW9Ou+E9I8MghOYFk4aYAebwZZOR8h77swAyHuOeuYC2586QdYjB4TARgRAAUBs&#13;";
        footer += "\r\njSlCYAax1eYwt9oM5lZx5nMAoPGVhtyfhalRGR9Wof+PM1fZF5MHQvPJI/KK8yzAU5eFoEGXgqmA&#13;";
        footer += "\r\noDyCIcXfRy6f5usrD6YXwhcOw8cIL5k8ysfHP2bm8ikb4w8ZviEAeCWiUr42hvUt9pDz2JkBkMtO&#13;";
        footer += "\r\nBTnD9sc4a3jkBJkPHCG9zx4hsP6rAiBulxnE7zTntMMC4mrNEQQVANEYCaKqDNkJYWa8QmHVkj+F&#13;";
        footer += "\r\n5Jrc9pvuM4/6njxQeDGbvCGPyCtWv6F3uiwFD3oxyMPf38nT338qVZP4pCwSsHTg7x/i6SvHfOPP&#13;";
        footer += "\r\nooGMQPCfFjNr5aRNsbuNe+Ydl/4nAZB4nBPtv6d74G3pmMiOgxEA2x9NYV/nPJgC2ais+5Mg/Z4D&#13;";
        footer += "\r\nbO6xQwCs/6IAICXuMUdZsL/2lVCH2mWBAFioAMAIEF2NhV+1gep4uBIA6e9n5xnfCUh0XUV9rUjD&#13;";
        footer += "\r\n4eQB84If+egRm72hZ+SdLheDBr0cTMUDDwGLBBhaPHwDAvFFpiN5LBpwIHBpQSbnQAhKcF8SVWN+&#13;";
        footer += "\r\nKeGY/u94CEjzGvQh+cQ4NMAC0m6Ph/TuSUoAsu87sfoh6x5Ghl4nyOiZyM4FbOkcz46K0bZw+kuj&#13;";
        footer += "\r\nny4AZihzSD5oDkkHLCD5AJ0N4I+E0cEQi34AxNHJoB1Y9NWOhQj6W4DCewOQ+VXSfwnJNjvrF+aZ&#13;";
        footer += "\r\nQn3Lh3tmPBeJg8kL8oQV7OgRbz55p8vl4EE3hAghoJBCecXDN9CXTwk03cAUMEsRgkIVuSiCKMWK&#13;";
        footer += "\r\nNNp/ln9iZLH1kfgjY/8bg6BBpYRj9GgAKaeNYeUVTAGtOHXsnawEILNnMmR0T2YHQ9I7J7E/UbO1&#13;";
        footer += "\r\nfRJsaZ8Im+46sj8dS+klrdmG/eFH2ha++qYVu3073ZVj1VXLUQDAHBaeMIfU45xSGmhrOKoe9ZUF&#13;";
        footer += "\r\nOxeQ+pUVpBy1hpQj1uxQyIJDVjD/IOqAFSTvt+oHQHwd5vxdRhC1U195X4AIocj4CoPfBG+x3C8L&#13;";
        footer += "\r\nDoiTKUI99a2vwnjqc9b3ipBPnpA35JHQfF02hAy6JYyHgEKJt3fgJJpTustk7tz8MkBGc00KPfRm&#13;";
        footer += "\r\n+PqAFiLQfFYj8FGBYAha6LoqstSiPnbvuNeJx/T/QACoqR4jxLGxsPBrUzSIDLRld96m419bMXXw&#13;";
        footer += "\r\nADAI6GbRbRPZwRD6A5Ab7ziyP+e+4Tbq1gRYfxN1YwL7w0qktXQq6Op4WH3FHlZftoNVTXaw8qIt&#13;";
        footer += "\r\nrGhEXSDZsXMBy0nsbIAdLDtjB0u/toWlp21hySnUSVtYfAJ13BYWNdCxMGtIJdEBESUAVmoAzNtv&#13;";
        footer += "\r\nwcJ/3G4jmFs3FqJ2STnThVIYH1al/3dz8k1uzVw9IZcf7VyODwqjPuXzPDfosM9Z3wfIyAvyhK3d&#13;";
        footer += "\r\noEfkldD8obaEDboplJ6AQoirXG7j6Rk4nlaU8MWmEGk0x6R8w4HgJ1e8qelcjTAtRBGiQnkYZAIY&#13;";
        footer += "\r\n/EP8E+ak25XG7DC5E3/E4O95AHjFk77ilFBvgGAYwoITxmykLm/CkX7NDtYTHGi8LgBwp4LGw5or&#13;";
        footer += "\r\n47UcDKEjYfbsRNAK0jm7YQGQctQSkg9jfj+Ahd5+I4jdOxaidxuo/Qn4SKGUxkv/EF5m+Dok06I+&#13;";
        footer += "\r\naJ7rSuobmcB06jvqQ+pLthqLfcv1sZ9c0efezAP0gjwhb8gj8op5RubruCl0QAgcHALHUf6gIoIq&#13;";
        footer += "\r\nySk+PnYUXhhp+KKenv5ThSBw4Qjzkb9/EIsKVCdowMCnCS46BDEggpe6rIkqsTgeu2fcy/jDBv8T&#13;";
        footer += "\r\nAfgzD4BSR1WKIx2hrxGOekNIahgH8xuM2YHOlJOmkIrhe9FpCukWCI0lLDlrBUvPWcOy89Y4ym1Q&#13;";
        footer += "\r\n/Ki3ZSNeeBJo4UkLRbjHPI85f3495XoTmHfYGBIPGUHCQSOIP2AIcfsMIGavVHn+P4pXnYYE5kfU&#13;";
        footer += "\r\n6P9DaKHxneD1diXyEHk8P8pZeNcwneV3WoXFvlT0qb/QeOp78oC8IE/IG/KIvCLPyDtdzR/wYIir&#13;";
        footer += "\r\nYnqI4cSYVpG4sCK3oYUFIQgKCllqoAUIKkgoRPmyNx4UxKcItkrFzVfnaALBIkQABwVFiFmrJ20O&#13;";
        footer += "\r\n226zO6rC/HJs3bhn8QcM/i7+iPSPQgDUdJhTLK9DAh3kNJfXAV7q5/tjeO3TEN3pQ6g9GlIH4M+R&#13;";
        footer += "\r\ntfr/EFFl+E1YkcmtOVmWh4NXj98eEDc1VabFcJkivLPVVl/e9KAgX1ZsT5NTX7I+VYR6fsTzxpMX&#13;";
        footer += "\r\nCk8sFR4ZK6b0BsM5GKI9EuhxEFAFSeGERQMnf3MGglwBgpvfBB8MP1R8UB6iKpQtI+MbZgWjXAUD&#13;";
        footer += "\r\nKxwxlNGCBRHOLVtOCxEAoR4lCIwALnUQGH7BAbHBy5zXz8mxrY0oN22M3mX0MHq34bu5+8b+l9j9&#13;";
        footer += "\r\nBr+NPaD/r2j4Hz46ALslf4yuk/6fyFqDv40oN7wflm96ISTdtnb6sskbAoJlcfSe+fevMps+m8Jw&#13;";
        footer += "\r\n/Mzc8ro8WMaHd7bOojBdTrMt1ndebLRjn1Lfcn3sM5H6nBnPeWBJnpA35BF5xRV8auaP+HzgGH6K&#13;";
        footer += "\r\nyIMg40HwV4CA9OGbsnN3lzvgvznS4gMfFWgpkoeBKObTRH8gAmdgp6lBoYoUQWpgCOHwE0aOgOks&#13;";
        footer += "\r\nejBQZgTOnR7nmTIzxXVl8CqnLSEbJuSGpttVhObY7AnLt6gPKzY/F1lmei2yzORWZInppfBCszPh&#13;";
        footer += "\r\n+RYNoTmWB+dkWtXN3mJXEbLBviBkjWPWzGWTNs1IdV0dmOi+OCDKZ57/DP8Yek0/xXvgTVY3Okg5&#13;";
        footer += "\r\nstXMxs8o4y7UqBuuCO/8SKc+o77jRzv1KfUt9TH1NT/i+XAvExgvmOqNGYn5A0JAUwhvRW0gBMHZ&#13;";
        footer += "\r\nOdDMF4sOutigqD5ZevBhRQkHAx8Z3GXT3Nl0hV1qVgChiBDYMQFCKCgU0ujoBwbKTwEHU4A6JELx&#13;";
        footer += "\r\nxoxUms+nNDdA8dooP4XJ/Y2my+jqZsvYJVpuhPOGK+onT+obfqTzprPRrgjzbEqOfUx9TX0uNJ48&#13;";
        footer += "\r\nURgvHQ3zNSFQBwHzihAEOyUIzmZUfRKZQhjYahTSSzmLzVF9fZ3ZMqUCCD5CePkFUXHj6xPAQYF5&#13;";
        footer += "\r\n0F8JRgAHBqsnCA4+aggih698OoNEKN4YocgwbdL2s5rPR68hHMn8aCbRe2NGBwiMxs/AzMbPRJ+N&#13;";
        footer += "\r\nPiM/wnnDqS+oT6hvqI+or7hpt8p06tNJzHhnM2GoVxqvyvWaxn+hNwrtC40CUQ0EPjXQYgMVIHxU&#13;";
        footer += "\r\n0AYDTVP46MADIVNECK8ALmXIFFBgp3nyYOD3fLTBwQPCQ8KD4iOfzkSm8OKN8htAQiN58c/DG8ub&#13;";
        footer += "\r\nyxuszWQZG9Wc0fQZ6LPQZ2IhPYAW0wJd6DPzhvOjnPpGu+ncaGfFHfaxWqjvb/yXo2m8biAoUgO9&#13;";
        footer += "\r\nKWFUEMLA1wvcRQm5jRAIol3GNqEEKaGgEOjrG+TMg8GWN7XAwQMihIQXGcKLQaPU9AGk+hnh7wqf&#13;";
        footer += "\r\nkzeXN1ibyWxUK4ymz+CuZnbQRJlihAsNpz6hvuHzutB04WgX5Pif1PjB0gIPQr+owMNA1PL1Ai1O&#13;";
        footer += "\r\naALhFhhoTdUsFTdCKGhUCMHwlMsnC+FQAkLb1wSQePhNx1+dzmDhRQYxBQ4hxc8Jf5eey4NJZS63&#13;";
        footer += "\r\nLM4ZLDSZ3qPQaPoMQrNZAYeflT6zpuHUN3xe50e6munqo12b8R/d/KEigjAqaIfBIXCcMDoIgfBV&#13;";
        footer += "\r\npAwGhRsHBY0O6jQOjiA1OISA8JDwoPAiU3hR2OU0fQBx/y78HeFz8c/Pvx5vsLrJQfb8+2Ujm8x2&#13;";
        footer += "\r\nU5lNn1HTcH6UU98MYrrmaP/y5zJ9+FFBAIMmEI6OwSxCCKHgwJhh6hyoAkMTDh4QEhphK1OAwu18&#13;";
        footer += "\r\nCbLnYVEqkANHF9HPCn+Xfz5+BPOvx78+/36EJiuNDiSjZ5jyn0tpNhvhwYaahg9g+icx2j8EBq1A&#13;";
        footer += "\r\nBAoihDoUXKQQgiGMGHzU4AERQsKDwotM8fCYribeME1p/hxvKC/ha/CvS++BH83CES00mh/ZQrN5&#13;";
        footer += "\r\nw6kPhjD8szB9KBCGBoKKGgEUmmAMBAdJJoBEGEWE4gyaoaNcTbU9h/A1ZIrXHshkTaM1zB6O4Z+d&#13;";
        footer += "\r\n8R8KhBAKthStCYYQDk1INGHh04xQDCAt0vw54XNoPr/m6wvfm8potZEt0fiMf/GGfygQ2sDQgIMU&#13;";
        footer += "\r\nroSEB0UTltEQ/7x6auaGS/q/n37vV9tn+qs0fKRQDAbHQJCMGRqcIaXLcw72ngb7LGIbRTB0hWQ0&#13;";
        footer += "\r\npev7EdsnCMloSWxiE5vYxCY2sYlNbGITm9jEJjaxiU1sYhOb2MQmNrGJTWxiE5vYxCa2T7X9fwnb&#13;";
        footer += "\r\nwIX+uKqMAAAAAElFTkSuQmCC</y:Resource>";
        footer += "\r\n    </y:Resources>";
        footer += "\r\n  </data>";
        footer += "\r\n</graphml>";
        return footer;
    }
}



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Asma
 */
public class Veille {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            // 1...charger le driver mysql
            Class.forName("com.mysql.jdbc.Driver");
            // 2..creer la conection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/veille2", "root", "");
            // 3..creer la requette
            Statement stm1 = con.createStatement();
            Statement stm2 = con.createStatement();
            Statement stm3 = con.createStatement();
            // 4..executer requette

            ResultSet res = stm1.executeQuery("select inventor from  brevet ;");
            // 5..parcour des resultat stoque dans res
            while (res.next()) {
                String champ = res.getString(1);
                if (champ != null) {
                    String[] tab = champ.split(";");
                    for (String el : tab) {
                        if (!el.isEmpty()) {
                            String[] nom = el.split("\\[");
                            String var = nom[0].trim();
                            
                            ResultSet rs1 = stm2.executeQuery("select min(idItem) from  items where LCASE(nom_long)=LCASE('" + var + "') ;");
                            if (rs1.next() && rs1.getInt(1)!=0) {
                                
                                System.out.println(stm2.executeUpdate("insert into items (nom_long,numero_item,idAnalyseChamp) values (UPPER('" + var + "'),'"+rs1.getInt(1)+"',19)"));
                            
                            } else {
                                
                                ResultSet rs2 = stm3.executeQuery("select max(idItem) from  items ");
                                rs2.next();
                                //System.out.println("id"+rs2.getInt(1)+"  +1" +(rs2.getInt(1)+1));
                                System.out.println(stm3.executeUpdate("insert into items (nom_long,numero_item,idAnalyseChamp) values ( '"+ var+"' ,'"+(rs2.getInt(1)+1)+"',19)"));
                            }
                            if (nom.length > 1) {
                                ResultSet rs3 = stm2.executeQuery("select min(idItem) from  items where nom_long='" + nom[1].substring(0, 2) + "' ;");
                                if (rs3.next() && rs3.getInt(1)!=0) {
                                    System.out.println(stm2.executeUpdate("insert into items(nom_long,numero_item,idAnalyseChamp) values ('" + nom[1].substring(0, 2) + "','"+rs3.getInt(1)+"',22)"));
                                } else {
                                    ResultSet rs4 = stm3.executeQuery("select max(idItem) from  items ");
                                    rs4.next();
                         
                                    System.out.println(stm3.executeUpdate("insert into items(nom_long,numero_item,idAnalyseChamp) values ('" + nom[1].substring(0, 2) + "','"+(rs4.getInt(1)+1)+"',22)"));
                                }
                            }
                            //System.out.println("id : "+nom[0].trim()+"   nom : "+nom[1].substring(0, 2));
                        }
                    }
                }
                //System.out.println("id :"+res.getInt(1)+"   nom : "+res.getString(2));
            }
            //  6..fermer la connection
            con.close();
            // 7..traitement des exeption
        } catch (Exception e) {
            System.out.println("ERROR :" + e.getMessage());
        }
        // TODO code application logic here
    }
}

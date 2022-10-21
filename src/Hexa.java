import epuletek.Epulet;
import epuletek.Fembanya;
import epuletek.Hajogyar;
import epuletek.Naperomu;
import urhajok.Anya;
import urhajok.Urhajo;
import urhajok.Vadasz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import static java.lang.Math.abs;

public class Hexa extends JPanel
{
    private final ArrayList<ArrayList<Shape>> cells = new ArrayList<>(6);
    private Point active = null;
    private final Dimension customSize;
    private final ArrayList<ArrayList<GameElement>> koordinata = new ArrayList<>();
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

    Player player1 = new Player("Player1");
    Player player2 = new Player("Player2");
    Player activePlayer = player1;
    int round = 0;
    public Hexa(Dimension meret) throws IOException
    {
        setLayout(new BorderLayout());
        customSize = meret;
        InitStatPanel
();
        InitPlanets();
        drawHexa();

        //Demo bolygó
        Bolygo planet = new Bolygo();
        planet.createBuilding(new Fembanya());
        planet.createBuilding(new Hajogyar());
        planet.createBuilding(new Naperomu());

        //Demo flotta
        Flotta demoFlotta = new Flotta();
        demoFlotta.addUrhajo(new Vadasz());
        demoFlotta.addUrhajo(new Vadasz());
        demoFlotta.addUrhajo(new Vadasz());
        demoFlotta.addUrhajo(new Vadasz());
        demoFlotta.addUrhajo(new Vadasz());
        demoFlotta.addUrhajo(new Anya());

        planet.addFlotta(demoFlotta);
        ShowStatPanel(planet);

        setFocusable(true);
        addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    round++;
                    if(activePlayer == player1){
                        player2.newRound(round);
                        activePlayer = player2;
                    }else{
                        player1.newRound(round);
                        activePlayer = player1;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {

                for(int i = 0; i < cells.size(); i++)     //  Végig megyünk az összes HexaShape-en
                {
                    for (int j = 0; j < cells.get(i).size(); j++) {
                        Shape currentShape = cells.get(i).get(j);
                        GameElement currentGameElement = koordinata.get(i).get(j);

                        if (currentShape.contains(mouseEvent.getPoint())) {    //  ha HexaShape-re kattintottunk
                            buttonPanel.setVisible(false);
                            if(active == null)                                 //  ha nincs aktív flotta...
                            {
                                if (currentGameElement instanceof Flotta) {      //  akkor megnézzük, hogy azon a Hexan van-e flotta
                                    active = new Point(i,j);
                                    System.out.println("Kijelöltünk egy flottát!");
                                    break;
                                }
                                if (currentGameElement instanceof Bolygo) {
                                    ShowStatPanel((Bolygo) currentGameElement, new Point(i, j));
                                    break;
                                }
                            }
                            else if (active != null)    //  ha van aktív flotta...
                            {
                                if(koordinata.get(active.x).get(active.y) instanceof Bolygo)
                                {
                                    move(new Point(i,j), (Bolygo) koordinata.get(active.x).get(active.y).getUrhajok());
                                }
                                if(move(new Point(i,j), null)){
                                    active = null;
                                }
                                else{
                                    System.out.println("Not enough fuel or action point");
                                }
                            }
                            break;
                        }
                    }

                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });
        setOpaque(false);

    }

    
    private Boolean move(Point to, ArrayList<Urhajo> urhajok){
        if(urhajok == null)
        {
            Integer fuelUsage = ((Flotta)koordinata.get(active.x).get(active.y)).getFuelUsage() * abs((to.x+to.y)-(active.x+active.y));
            Integer actionPointUsage = ((Flotta)koordinata.get(active.x).get(active.y)).getActionPointUsage() * abs((to.x+to.y)-(active.x+active.y));

            GameElement currentGameElement = koordinata.get(to.x).get(to.y);
            GameElement activeGameElement = koordinata.get(active.x).get(active.y);
            if(activePlayer.getFuel() - fuelUsage >= 0 && activePlayer.getActionPoint() - actionPointUsage >= 0) {
                if (currentGameElement instanceof Bolygo) {  //  Ha bolygóra kattintottunk, akkor hozzáadjuk a flottát a bolygóhoz
                    ((Bolygo) currentGameElement).addFlotta((Flotta) activeGameElement);
                    System.out.println("Hozzáadunk a bolygójoz egy flottát!");
                    koordinata.get(active.x).set(active.y, null);
                }
                else if (currentGameElement instanceof Flotta) {  // Ha egy másik flottára kattintunk, akkor
                    if (activePlayer.ownedGameElements.contains(currentGameElement) && activePlayer.ownedGameElements.contains(active)){ // Ha saját flottája a játékosnak akkor merge
                        ((Flotta) currentGameElement).mergeFlotta((Flotta) activeGameElement);
                    }
                    else{ // Különben harc
                        battle( (Flotta) activeGameElement, (Flotta) currentGameElement);
                    }
                }
                else{ // üres cellára kattintva csak mozgás
                    koordinata.get(to.x).set(to.y, koordinata.get(active.x).get(active.y));
                    koordinata.get(active.x).set(active.y, null);
                }
                activePlayer.useFuel(fuelUsage);
                activePlayer.useActionPoint(actionPointUsage);
                return true;
            }
            return false;
        }
        else
        {
            //TODO: ÁRON - CSINÁLD MEG, HOGY HA EGY BOLYGORÓL KATTINTUNK A KILÖVÉSRE, AKKOR A KIJELÖLT HEXÁRA KERÜLJÖN A BOLYGÓN LÉVŐ ÖSSZES HAJÓ
        }

        return null;
    }

    private void ShowStatPanel(Bolygo planet, Point pos)
    {
        buttonPanel.setVisible(true);

        for(Component component : buttonPanel.getComponents())
                buttonPanel.remove(component);

        //Épületek kihelyezése
        for(int i = 0; i < planet.epuletek.size(); i++)
        {
            Epulet epuletElement = planet.epuletek.get(i);
            String tipus = epuletElement instanceof Fembanya ? "Fembanya" : epuletElement instanceof Naperomu ? "Naperomu" : "Hajogyar";
            int szint = planet.epuletek.get(i).getSzint();

            //JPanel
            JPanel epulet = new JPanel();
            epulet.setPreferredSize(new Dimension(200, 200));

            //Kép
            ImageLabel label = new ImageLabel(new ImageIcon("pictures/battleship.png"));
            epulet.add(label);

            //Gomb
            if(tipus.equals("Hajogyar"))
            {
                JButton gyartas = new JButton("Gyártas");
                epulet.add(gyartas);
                gyartas.addActionListener(e->
                {
//                        TODO: MARTIN - CSINÁLD MEG, HOGY KITUDJA VÁLASZTANI, HOGY MILYEN HAJÓT ÉPÍTSEN!
//                        if(activePlayer.getMetal() >= (Hajogyar)epuletElement)
//                        {
//                            activePlayer.spendMetal(epuletElement.getCostToUpgrade());
//                            epuletElement.upgrade();
//                        }
                });
            }

            JButton gomb = new JButton("Fejlesztés");
            gomb.addActionListener(e->{
                if(activePlayer.getMetal() >= epuletElement.getCostToUpgrade())
                {
                    activePlayer.spendMetal(epuletElement.getCostToUpgrade());
                    epuletElement.upgrade();
                }
            });
            epulet.add(gomb);

            //Label
            JLabel nevLabel = new JLabel(tipus + " lvl " + szint);
            epulet.add(nevLabel);

            buttonPanel.add(epulet);
        }

        JButton release = new JButton("Hajók kilövése!");
        release.addActionListener(actionEvent->{
            if(active == null)
            {
                active = pos;
            }
        });
        buttonPanel.add(release);

        for(int i = 0; i < planet.getUrhajok().size(); i++)
        {
            //TODO: MARTIN - HELYEZD KI A HAJÓKAT A PANELRE
        }
    }

    private void InitStatPanel()
    {
        buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        buttonPanel.setBackground(new Color(1, 0.5f, 1, 0.3f));
        buttonPanel.setPreferredSize(new Dimension(200, 200));
        add(buttonPanel,BorderLayout.PAGE_END);
        buttonPanel.setVisible(false);
    }

    private void InitPlanets() throws IOException
    {
        int numberOfEmptyPlanets = (int) ((Math.random() * (40-20))+20);
        int planetCounter = 0;

        Flotta firstFlotta = new Flotta();
        firstFlotta.addUrhajo(new Anya());
        for(int i = 0; i < 20; i++)
        {
            koordinata.add(new ArrayList<GameElement>());
            for(int j = 0; j < 32; j++)
            {

                if(i == 9 && j == 9){
                    // Init player1
                    koordinata.get(i).add(new Bolygo());
                    GameElement currentGameElement = koordinata.get(i).get(j);
                    ((Bolygo) currentGameElement).setColonized(player1);
                    ((Bolygo) currentGameElement).createBuilding(new Fembanya());
                    ((Bolygo) currentGameElement).createBuilding(new Naperomu());
                    ((Bolygo) currentGameElement).createBuilding(new Fembanya());
                    ((Bolygo) currentGameElement).addFlotta(firstFlotta);
                    player1.addGameElement(firstFlotta);
                }
                else if (i == 3 && j == 22){
                    // Init player2
                    koordinata.get(i).add(new Bolygo());
                    GameElement currentGameElement = koordinata.get(i).get(j);
                    ((Bolygo) currentGameElement).setColonized(player2);
                    ((Bolygo) currentGameElement).createBuilding(new Fembanya());
                    ((Bolygo) currentGameElement).createBuilding(new Naperomu());
                    ((Bolygo) currentGameElement).addFlotta(firstFlotta);
                    player2.addGameElement(firstFlotta);

                }
                else if ( (int) ((Math.random() * (50-1))+1) == 1 && planetCounter < numberOfEmptyPlanets)
                    koordinata.get(i).add(new Bolygo());
                else if(i == 10 && j == 10)
                    koordinata.get(i).add(new Flotta());
                else
                    koordinata.get(i).add(null);

            }
        }
    }

    protected void drawHexa()
    {
        GeneralPath path = new GeneralPath();

        double rowHeight = ((customSize.height * 1.14f) / 3f);
        double colWidth = customSize.width / 3f;

        double size = Math.min(rowHeight, colWidth) / 12d;   //TODO: EZ EGY HEXAGON MÉRETÉT ADJA MEG, MINÉL TÖBBEL OSZTASZ ANNÁL KISEBB LESZ A HEXAGON!

        double centerX = size / 2d;
        double centerY = size / 2d;

        double width = Math.sqrt(3d) * size;
        double height = size * 2;

        for(float i = 0; i < 6; i++)
        {
            float angleDegrees = (60f * i)-30f;
            float angleRad = ((float) Math.PI / 180.0f) * angleDegrees;

            double x = centerX+(size * Math.cos(angleRad));
            double y = centerY+(size * Math.sin(angleRad));

            if(i == 0)
            {
                path.moveTo(x, y);
            } else
            {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        cells.clear();
        double yPos = size / 2d;

        for(int row = 0; row < 20; row++) //TODO: ITT ÁLLÍTSD ÁT HÁNY SOROS LEGYEN
        {
            cells.add(new ArrayList<Shape>());
            double offset = (width / 2d);
            if(row % 2 == 0)
            {
                offset = 0;
            }
            double xPos = offset;
            for(int col = 0; col < 32; col++) //TODO: ITT MEG HOGY HÁNY OSZLOPOS
            {
                AffineTransform at = AffineTransform.getTranslateInstance(xPos+(size * 0.38), yPos);
                Area area = new Area(path);
                area = area.createTransformedArea(at);
                cells.get(row).add(area);
                xPos += width;
            }
            yPos += height * 0.75;
        }
    }

    private void battle(Flotta attacker, Flotta defender) {
        Random rand = new Random();
        int attackerIndex = 0;
        int defenderIndex = 0;
        boolean theyAreAlive = true;
        while (theyAreAlive){
            int attackerSize = attacker.getUrhajok().size();
            int defenderSize = defender.getUrhajok().size();
            for (int switcher = 0; switcher < 2; switcher++){
                if (switcher == 0){ // attackers turn
                    while (!attacker.getUrhajok().get(attackerIndex).isAlive()) { // The attacker ship is already down
                        attackerIndex++;
                    }
                    int whoToAttack = rand.nextInt(defenderSize);
                    while (!defender.getUrhajok().get(whoToAttack).isAlive()){ // The ship we want to attack is already down
                        whoToAttack = rand.nextInt(defenderSize);
                    }
                    defender.getUrhajok().get(whoToAttack).getHit(attacker.getUrhajok().get(attackerIndex).getTamadas() - defender.getUrhajok().get(whoToAttack).getVedekezes());
                }
                else{ // defenders turn
                    while (!defender.getUrhajok().get(defenderIndex).isAlive()) { // The attacker ship is already down
                        defenderIndex++;
                    }
                    int whoToAttack = rand.nextInt(attackerSize);
                    while (!attacker.getUrhajok().get(whoToAttack).isAlive()){ // The ship we want to attack is already down
                        whoToAttack = rand.nextInt(attackerSize);
                    }
                    attacker.getUrhajok().get(whoToAttack).getHit(defender.getUrhajok().get(defenderIndex).getTamadas() - attacker.getUrhajok().get(whoToAttack).getVedekezes());
                }
            }
            attackerIndex++;
            defenderIndex++;
            if (attackerIndex > attackerSize-1)
                attackerIndex = 0;
            if (defenderIndex > defenderSize-1)
                defenderIndex = 0;
            boolean attackersAreDown = true;
            for(int i = 0; i < attackerSize; i++){
                if(attacker.getUrhajok().get(i).isAlive())
                    attackersAreDown = false;
            }
            boolean defendersAreDown = true;
            for(int i = 0; i < defenderIndex; i++){
                if(attacker.getUrhajok().get(i).isAlive())
                    defendersAreDown = false;
            }
            if(attackersAreDown || defendersAreDown)
                theyAreAlive = false;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.BLACK);
        int x, y;
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                x = (int) cells.get(i).get(j).getBounds2D().getX();
                y = (int) cells.get(i).get(j).getBounds2D().getY();
                g2d.draw(cells.get(i).get(j));

                if(koordinata.get(i).get(j) instanceof  Bolygo){
                    g2d.drawImage(koordinata.get(i).get(j).getKep(), x, y, this);
                }
                else if(koordinata.get(i).get(j) instanceof  Flotta){
                    g2d.drawImage(koordinata.get(i).get(j).getKep(), x, y, this);
                }
            }
        }
        g2d.dispose();

    }

    @Override
    public Dimension getPreferredSize() {return customSize;}

    static class ImageLabel extends JLabel {

        public ImageLabel(String img) {
            this(new ImageIcon(img));
        }

        public ImageLabel(ImageIcon icon) {
            setIcon(icon);
            // setMargin(new Insets(0,0,0,0));
            setIconTextGap(0);
            // setBorderPainted(false);
            setBorder(null);
            setText(null);
            setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
        }

    }
}
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it.
 **/
class Player {
    private static List<EntityGame> arrayWizard;
    private static List<EntityGame> arrayEnemyWizard;
    private static List<EntityGame> arraySnaffle;
    private static List<EntityGame> arrayBludger;
    private static Map<Integer, Map<Integer, Integer>> goal =  new HashMap<Integer, Map<Integer, Integer>>() {{
        put(0, new HashMap<Integer, Integer>() {{ put(0, 16000); put(1, 3750);}});
        put(1, new HashMap<Integer, Integer>() {{ put(0, 0); put(1, 3750);}});
    }};
    private static EntityGame cibled = null;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        // game loop
        while (true) {
            arrayWizard = arrayBludger= new ArrayList<EntityGame>();
            arrayEnemyWizard = new ArrayList<EntityGame>();
            arraySnaffle = new ArrayList<EntityGame>();

            int myScore = in.nextInt();
            int myMagic = in.nextInt();
            int opponentScore = in.nextInt();
            int opponentMagic = in.nextInt();
            int entities = in.nextInt(); // number of entities still in game
            for (int i = 0; i < entities; i++) {
                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise

                if(entityType.equals("WIZARD")){
                    arrayWizard.add(new EntityGame(entityId,x,y,vx,vy, state));
                }else if(entityType.equals("OPPONENT_WIZARD")){
                    arrayEnemyWizard.add(new EntityGame(entityId,x,y,vx,vy, state));
                }else if(entityType.equals("SNAFFLE")){
                    arraySnaffle.add(new EntityGame(entityId,x,y,vx,vy, state));
                }
            }
            for (EntityGame wizard: arrayWizard) {

                if(wizard.getState() == 1){
                    System.out.println("THROW "+goal.get(myTeamId).get(0)+" "+goal.get(myTeamId).get(1)+" 500");
                }else if(wizard.getState() == 0 && ( (arraySnaffle.size()>0 && cibled==null) || (arraySnaffle.size()>1 && cibled!=null) )){
                    EntityGame snaf = getNearestSnaffle(wizard);
                    cibled = snaf;
                    System.out.println("MOVE "+snaf.getX()+" "+snaf.getY()+" 150");
                }else{
                    System.out.println("MOVE 8000 3750 100");
                }

            }
            cibled = null;
        }
    }

    private static EntityGame getNearestSnaffle(EntityGame wizard){
        arraySnaffle.forEach(e -> e.setDistance(getDistanceBetweenEntity(e, wizard)));
        return arraySnaffle.stream().filter(snaffle -> snaffle != cibled).min(Comparator.comparingInt(EntityGame::getDistance)).get();
    }

    private static int getDistanceBetweenEntity(EntityGame a, EntityGame b){
        double distance = Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
        return (int) distance;
    }
}


class EntityGame{
    private int id;
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int state;
    private int distance;

    EntityGame(int id, int x, int y, int vx, int vy, int state){
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}

import java.util.*;
import java.io.*;
import java.math.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Ranked at 1451/3508 (so bad)


// Usines
class FACTORY{
    int entityId;
    int owner;
    int nbrCyborgs;
    int prodUsine;
    
    Map<FACTORY, Integer> dists = new HashMap<FACTORY, Integer>();
    
    public FACTORY(){};
    
    public FACTORY(int entityId) {
        this.entityId = entityId;
    }
    
    boolean isMine() {
        return owner == 1;
    };
    
    @Override
    public String toString() {
        return entityId + " ";
    }
}   

// Soldats
class TROOP{
    int entityId;
    int idFactStart;
    int idFactEnd;
    int nbrCyborgTROOP;
    int nbrTourArrive;
    public TROOP(int entityId, int arg2,int arg3,int arg4,int arg5) {
        this.entityId = entityId;
        this.idFactStart = arg2;
        this.idFactEnd = arg3;
        this.nbrCyborgTROOP = arg4;
        this.nbrTourArrive = arg5;
    }
}

/**
 *
 * @author guillaumeboutin
 */
class Player {
    public static int tour = 0;
    public static int  Bombs = 0;
    public static List<TROOP> allTROOPs;
    public static int trooperDispo = 0;
    private static Map<Integer, FACTORY> factoryMap;
    private static ArrayList<FACTORY> myFactories;
    private static ArrayList<FACTORY> otherFactories;
    private static ArrayList<FACTORY> allFactories;
    private static List<Integer> shootedFactories;
    private static List<Integer> usedFactories;
    private static String attack;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int factoryCount = in.nextInt(); // the number of factories
        int linkCount = in.nextInt(); // the number of links between factories
        factoryMap = new HashMap<Integer, FACTORY>();
        shootedFactories = new ArrayList<>();
        for (int i = 0; i < linkCount; i++) {
            int factory1 = in.nextInt();
            int factory2 = in.nextInt();
            int distance = in.nextInt();
            addMapping(factory1, factory2, distance); // Enregistre 
        }

        // game loop
        while (true) {
            tour++;
            attack = "";
            usedFactories = new ArrayList<>();
            // A REVOIR
            allTROOPs = new ArrayList<TROOP>();
            
            int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();
                int arg5 = in.nextInt();
                
                if(entityType.equals("FACTORY")){
                    addFACTORYParams(entityId,arg1, arg2, arg3);
                }else if(entityType.equals("TROOP") && arg1==1){
                    TROOP troop = new TROOP(entityId, arg2, arg3, arg4, arg5);
                    allTROOPs.add(troop);
                }   
            }
           
            Stream<FACTORY> test = myFactories();
            myFactories = test.collect(Collectors.toCollection(ArrayList::new));
            Collections.reverse(myFactories);
            
            Stream<FACTORY> test2 = otherFactories();
            otherFactories = test2.collect(Collectors.toCollection(ArrayList::new));
          
            Stream<FACTORY> test3 = allFactories();
            allFactories = test3.collect(Collectors.toCollection(ArrayList::new));
            
            
            Attack();
            
        }
    }
         
    // ajouter a la Map
    private static void addMapping(int f1, int f2, int dist) {
        FACTORY factory1 = getOrCreate(f1);
        FACTORY factory2 = getOrCreate(f2);
        factory1.dists.putIfAbsent(factory2, dist);
        factory2.dists.putIfAbsent(factory1, dist);
        //err("Distance entre :"+f1+" et "+f2+" est de "+dist);
    }
    
    private static void addFACTORYParams(int to, int owner, int numberOfCyborgs, int factoryProduction) {
        FACTORY factory = factoryMap.get(to);
        factory.owner = owner;
        factory.nbrCyborgs = numberOfCyborgs;
        factory.prodUsine = factoryProduction;
    }
    
    private static FACTORY getOrCreate(int factoryId) {
        if (factoryMap.containsKey(factoryId)) {
            return factoryMap.get(factoryId);
        } else {
            FACTORY factory = new FACTORY();
            factory.entityId = factoryId;
            factoryMap.put(factoryId, factory);
            return factory;
        }
    }
    

    private static void Attack(){
        ActionMove();
        ActionBomb();
        //ActionInc();
        attack = attack + "WAIT";
        System.out.println(attack);
    }
    
    /*for(FACTORY myfact: myFactories){
            int troopDispo = myfact.nbrCyborgs;
            if(otherFactories.size()>0){
                err("JE TRI:");
                orderDest(myfact, otherFactories);
                for(FACTORY fact: otherFactories){
                    int troops = getFutureTROOP(myfact,fact);
                    if(troopDispo > troops){
                        troopDispo = troopDispo - troops;
                        attack = attack + "MOVE "+myfact.entityId+" "+fact.entityId+" "+troops+"; ";
                    }else if(Bombs <2  && fact.owner==-1 && !shootedFactories.contains(getStatsBomb()) ){
                        Bombs++;
                        shootedFactories.add(getStatsBomb());
                        attack = attack + "BOMB "+myfact.entityId+" "+getStatsBomb()+"; ";
                    }else if(troopDispo>10 && myfact.prodUsine<3){
                        attack = attack + "INC "+myfact.entityId+"; ";
                    }else{
                        attack = attack + "WAIT; ";
                    }
                }
            }
        }*/
    
    // Action Move
    private static void ActionMove(){
        for(FACTORY myfact: myFactories){
            int troopDispo = myfact.nbrCyborgs;
            orderDest(myfact, allFactories);
            for(FACTORY fact: allFactories){
                if(myfact.entityId != fact.entityId){
                    err("Troop dispo : "+troopDispo+" dans mon usine "+myfact.entityId);
                    if(troopDispo>0){
                        err("fact : "+fact.entityId+ " distance "+ getDistance(myfact, fact) );
                        if(myfact.nbrCyborgs >=10 && myfact.prodUsine<3 ){ //&& getTROOPersSendInThatFact(myfact.entityId, -1)== 0
                            troopDispo = troopDispo - 10;
                            myfact.nbrCyborgs = troopDispo;
                            ActionInc(myfact);
                        }
                        if(troopDispo > (fact.nbrCyborgs+1)){   // 
                            troopDispo = troopDispo - (fact.nbrCyborgs+1);
                            myfact.nbrCyborgs = troopDispo;
                            attack = attack + "MOVE "+myfact.entityId+" "+fact.entityId+" "+(fact.nbrCyborgs+1)+"; ";
                        }else if(troopDispo > myfact.prodUsine){
                            troopDispo = troopDispo - myfact.prodUsine;
                            myfact.nbrCyborgs = troopDispo;
                            attack = attack + "MOVE "+myfact.entityId+" "+fact.entityId+" "+myfact.prodUsine+"; ";
                        }else if(myfact.nbrCyborgs >= 2 && myFactories.size()>0 && myFactories.get(myFactories.size()-1).entityId != myfact.entityId){
                            usedFactories.add(myfact.entityId);
                            orderDest(myfact, myFactories);
                            attack = attack + "MOVE "+myfact.entityId+" "+myFactories.get(myFactories.size()-1).entityId+" "+(myfact.nbrCyborgs-2)+"; ";
                        }else{
                            attack = attack + "WAIT;";
                        }
                    }
                }
            }
        }
    }
    
    // Action Bomb
    private static void ActionBomb(){
        if(myFactories.size()>0 && otherFactories.size()>0 && Bombs <2  
                && getStatsBomb() != null 
                && getStatsBomb().owner==-1 
                && !shootedFactories.contains(getStatsBomb().entityId) ){
            Bombs++;
            shootedFactories.add(getStatsBomb().entityId);
            attack = attack + "BOMB "+myFactories.get(0).entityId+" "+getStatsBomb().entityId+"; ";
        }
    }
    
    // Action Inc
    private static void ActionInc(FACTORY myfact){
        attack = attack + "INC "+myfact.entityId+"; ";
    }
    
    // Get mines
    private static Stream<FACTORY> myFactories() {
        return factoryMap.values().stream().filter(FACTORY::isMine);
    }
    
    // get Other
    private static Stream<FACTORY> otherFactories() {
        return factoryMap.values().stream().filter(f -> !f.isMine() && f.prodUsine>0);
    }
    
    // get Other
    private static Stream<FACTORY> allFactories() {
        return factoryMap.values().stream();
    }
    
    // get incoming, destination and 1 or -1
    private static int getFutureTROOP(FACTORY myfact, FACTORY enemyFact){
        //return enemyFact.nbrCyborgs + getDistance(myfact, enemyFact) + getDistance(myfact, enemyFact)*enemyFact.prodUsine + getTROOPersSendInThatFact(enemyFact.entityId, -1)  + getTROOPersSendInThatFact(enemyFact.entityId, 1);
        return enemyFact.nbrCyborgs + getDistance(myfact, enemyFact);//*10
    }
    
    
    // Order by
    private static List<FACTORY> orderDest(FACTORY source, List<FACTORY> Factories) {
        Factories.stream().sorted((object1, object2) -> Long.compare(getStatus(source, object2), getStatus(source, object1)));//.forEachOrdered(System.err::println)
        return Factories;
    }
    
    // Get Stats
    private static int getStatus(FACTORY object1, FACTORY object2){
        //err("Distance entre :"+object1.entityId+" et "+object1.entityId+" est de "+getDistance(object1, object2));
        int stat =  getDistance(object1, object2) + object1.nbrCyborgs - getTROOPersSendInThatFact(object1.entityId, 1) + getTROOPersSendInThatFact(object1.entityId, -1) + object1.prodUsine;
        if(object2.owner == -1){
            stat = stat + object1.prodUsine * getDistance(object1, object2);
        }
        //err("Stat :"+stat);
        return stat;
    }
    
    // Get Distance
    private static int getDistance(FACTORY f1, FACTORY f2){
        return f1.dists.get(f2);
    }   
    
    // Get Stats Bomb
    private static FACTORY getStatsBomb(){
        int stat = 0;
        FACTORY fac = null;
        for(FACTORY fact: otherFactories){
            if(fact.prodUsine>1 && fact.nbrCyborgs + fact.prodUsine * 10> stat && fact.owner==-1){
                stat = fact.nbrCyborgs + fact.prodUsine * 10;
                fac = fact;
            }       
        }
        return fac;
    }
    
    // Get List
    private static List<FACTORY> getFact(int i){
        List<FACTORY> ret = new ArrayList<>();
        for(FACTORY fact: otherFactories){
            if(fact.owner == i){
                ret.add(fact);
            }
        }
        return ret;
    }
    
    // TROOPer en route
    private static int getTROOPersSendInThatFact(int idFact, int mine){
        int nbrSend = 0;
        for(TROOP troop: allTROOPs){
            if(troop.idFactEnd == idFact && mine == 1){
                nbrSend += troop.nbrCyborgTROOP;
            }
            if(troop.idFactEnd == idFact && mine == -1){
                nbrSend += troop.nbrCyborgTROOP;
            }
        }
        return nbrSend;
    }
      
    // Fact vide
    public static FACTORY getNullFac(List<FACTORY> myFactories){
        FACTORY ret = null;
        for(FACTORY fact: myFactories){
            if(fact.nbrCyborgs==0){
                return fact;
            }
        }
        return ret;
    }
    
    
    public final static void err(String s) {
        System.err.println(s);
    }
}


/*
else if((troopDispo + getTROOPersSendInThatFact(myfact.entityId, 1) - getTROOPersSendInThatFact(myfact.entityId, -1) - 2)>0 && troopDispo>2){
                        //usedFactories.add(myfact.entityId);
                        troopDispo = troopDispo -2;
                        myfact.nbrCyborgs = troopDispo;
                        attack = attack + "MOVE "+myfact.entityId+" "+fact.entityId+" "+2+"; ";
                    }


            FACTORY bombFact = getNullFac(myFactories);
            if(otherFactories.size()>0 && myFactories.size()>0 && bombFact != null && getTROOPersSendInThahtFact(bombFact.entityId)==0){
                if(Bombs<2 && bombFact.nbrCyborgs==0){
                    Bombs++;
                    attack = attack + "BOMB "+bombFact.entityId+" "+otherFactories.get(0).entityId+"; ";
                }
            }*/
            
            /*if(troopDispo>2){
                        troopDispo = troopDispo-2;
                        attack = attack + "MOVE "+myfact.entityId+" "+fact.entityId+" 2; ";
                    }else if(getTROOPersSendInThatFact(fact.entityId, -1)>troopDispo){
                        attack = attack + "WAIT; ";
                    }
                    */

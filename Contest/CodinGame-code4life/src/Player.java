import java.util.*;

/**
 * Created by guillaumeboutin on 15/05/2017.
 */

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 **/
class Player {
    private static int turn;

    public static void main(String args[]) {
        GamePlay gamePlay = new GamePlay(0, 0, 0, 0, 0);
        Scanner in = new Scanner(System.in);
        int projectCount = in.nextInt();
        for (int i = 0; i < projectCount; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int c = in.nextInt();
            int d = in.nextInt();
            int e = in.nextInt();
        }

        // game loop
        while (true) {
            turn += 2;
            boolean wait = false;
            List<Sample> minesSamples = new ArrayList<>();
            List<Sample> enemySamples = new ArrayList<>();
            List<Sample> cloudSamples = new ArrayList<>();
            List<Integer> storageFactory = new ArrayList<>();
            List<Integer> expertize = new ArrayList<>();
            List<Integer> avaibleMolecules = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                String target = in.next();
                int eta = in.nextInt();
                if (i == 0 && eta == 0) {
                    gamePlay.setPosition(target);
                    System.err.println("Ma position est : " + gamePlay.getPosition());
                }else{
                    gamePlay.setEnemyPosition(target);
                    System.err.println("La position enemi est : " + gamePlay.getPosition());
                }
                if(i == 0 && eta != 0){
                    System.err.println("Ma position est : wait");
                    wait = true;
                }
                int score = in.nextInt();
                int storageA = in.nextInt();
                int storageB = in.nextInt();
                int storageC = in.nextInt();
                int storageD = in.nextInt();
                int storageE = in.nextInt();

                storageFactory.add(storageA);
                storageFactory.add(storageB);
                storageFactory.add(storageC);
                storageFactory.add(storageD);
                storageFactory.add(storageE);
                gamePlay.setStorage(storageFactory);

                int expertiseA = in.nextInt();
                int expertiseB = in.nextInt();
                int expertiseC = in.nextInt();
                int expertiseD = in.nextInt();
                int expertiseE = in.nextInt();
                if (i == 0) {
                    expertize.add(expertiseA);
                    expertize.add(expertiseB);
                    expertize.add(expertiseC);
                    expertize.add(expertiseD);
                    expertize.add(expertiseE);
                    gamePlay.setExpertize(expertize);
                }

            }
            int availableA = in.nextInt();
            int availableB = in.nextInt();
            int availableC = in.nextInt();
            int availableD = in.nextInt();
            int availableE = in.nextInt();

            System.err.println(availableA + " " + availableB + " " + availableC + " " + availableD + " " + availableE);

            avaibleMolecules.add(availableA);
            avaibleMolecules.add(availableB);
            avaibleMolecules.add(availableC);
            avaibleMolecules.add(availableD);
            avaibleMolecules.add(availableE);

            int sampleCount = in.nextInt();
            for (int i = 0; i < sampleCount; i++) {
                int sampleId = in.nextInt();
                int carriedBy = in.nextInt();
                int rank = in.nextInt();
                String expertiseGain = in.next();
                int health = in.nextInt();
                int costA = in.nextInt();
                int costB = in.nextInt();
                int costC = in.nextInt();
                int costD = in.nextInt();
                int costE = in.nextInt();
                if (carriedBy == 0) {
                    minesSamples.add(new Sample(sampleId, health, costA, costB, costC, costD , costE,
                            costA - expertize.get(0), costB - expertize.get(1), costC - expertize.get(2), costD - expertize.get(3), costE - expertize.get(4)));
                } else if (carriedBy == 1) {
                    enemySamples.add(new Sample(sampleId, health, costA, costB, costC, costD, costE, costA, costB, costC, costD, costE));
                } else {
                    cloudSamples.add(new Sample(sampleId, health, costA, costB, costC, costD, costE, costA, costB, costC, costD, costE));
                }
            }
            if(wait){
                System.out.println("WAIT");
            }else{
                String action = gamePlay.getAction(minesSamples, avaibleMolecules, turn);
                gamePlay.doAction(minesSamples, action, avaibleMolecules);
                gamePlay.showResolutions();
            }
        }
    }
}

class GamePlay {

    private String position;
    private String enemyPosition;
    private int carryA;
    private int carryB;
    private int carryC;
    private int carryD;
    private int carryE;
    private List<Integer> expertize;
    private int expertizeA;
    private int expertizeB;
    private int expertizeC;
    private int expertizeD;
    private int expertizeE;
    private boolean resolveSample1;
    private boolean resolveSample2;
    private boolean resolveSample3;
    private List<Integer> usedForNextLabo;

    private String escapeCloud = "";

    public GamePlay(int a, int b, int c, int d, int e) {
        this.carryA = a;
        this.carryB = b;
        this.carryC = c;
        this.carryD = d;
        this.carryE = e;
        this.resolveSample1 = false;
        this.resolveSample2 = false;
        this.resolveSample3 = false;
        this.usedForNextLabo = new ArrayList<>();
        this.usedForNextLabo.add(0);
        this.usedForNextLabo.add(0);
        this.usedForNextLabo.add(0);
        this.usedForNextLabo.add(0);
        this.usedForNextLabo.add(0);
    }

    public String getPosition() {
        return this.position;
    }
    public void setPosition(String s) {
        this.position = s;
    }
    public String getenemyPosition() {
        return this.enemyPosition;
    }
    public void setEnemyPosition(String s) {
        this.enemyPosition = s;
    }


    public void showResolutions() {
        System.err.println(this.resolveSample1 + " " + this.resolveSample2 + " " + this.resolveSample3);
    }

    private int getTotalMolecules() {
        return carryA + carryB + carryC + carryD + carryE;
    }


    public void setStorage(List<Integer> storage) {
        this.carryA = storage.get(0);
        this.carryB = storage.get(1);
        this.carryC = storage.get(2);
        this.carryD = storage.get(3);
        this.carryE = storage.get(4);
    }

    public void setExpertize(List<Integer> expertize) {
        this.expertizeA = expertize.get(0);
        this.expertizeB = expertize.get(1);
        this.expertizeC = expertize.get(2);
        this.expertizeD = expertize.get(3);
        this.expertizeE = expertize.get(4);

        this.expertize = expertize;
    }

    public String getAction(List<Sample> minesSamples, List<Integer> avaibleMolecules, int turn) {
        String action;
        orderSample(minesSamples);
        if (minesSamples.size()>0 && testCanDoNow(minesSamples.get(0))) {
            this.resolveSample1 = true;
            System.err.println("AVANT 1");
        }else {
            this.resolveSample1 = false;
        }
        if (minesSamples.size()>1 && testCanDoNow(minesSamples.get(1))) {
            this.resolveSample2 = true;
            System.err.println("AVANT 2");
        }else{
            this.resolveSample2 = false;
        }

        if (minesSamples.size()>2 &&  testCanDoNow(minesSamples.get(2))) {
            this.resolveSample3 = true;
            System.err.println("AVANT 3");
        }else{
            this.resolveSample3 = false;
        }

        boolean cando = false;
        if (minesSamples.size()>0 && (molleculeAllDispo(minesSamples.get(0), avaibleMolecules) || this.resolveSample1)) {
            cando =true;
        }
        if (minesSamples.size()>1 && (molleculeAllDispo(minesSamples.get(1), avaibleMolecules) || this.resolveSample2)) {
            cando =true;
        }
        if (minesSamples.size()>2 &&  (molleculeAllDispo(minesSamples.get(2),avaibleMolecules) || this.resolveSample3)) {
            cando =true;
        }

        // GOGOGOGOGO SUPER MACHINE STAres/values/strings.xml

        // si les 3 sont résolus
        // si je peux en faire que 1 ou 2 et Mollecule == 10
        // je peux faire les 3
        // si je peux en faire que 1 ou 2 et Mollecule
        // si je peux en faire aucun et jen ai 3 sur moi
        // si je peux en faire aucun et jen ai 2 ou moins sur moi
        // si ma liste de sample est vide
        // si ma liste de sample < 3 et que ma position == SAMPLES
        // si ma quantité disgnositqué < quantité de samples
        // si ma quantité disgnositqué == quantité de samples
        // else ???
        if (!cando && minesSamples.size()>0 && (countDiag(minesSamples) == 3) && getPosition().equals("MOLECULES") && (getenemyPosition().equals("LABORATORY") || getenemyPosition().equals("MOLECULES")) ){ // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            action = "WAIT";
        } else if (!cando && (countDiag(minesSamples) == 3)) { // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            action = "CLOUD";
        } else if (turn>= 370  && (this.resolveSample1 || this.resolveSample2 || this.resolveSample3)) { // ------ Si debut late game jessaie den faire au moins 2 // GO TO LABORATORY
            action = "LABORATORY";
        } else if(getPosition().equals("LABORATORY") && (this.resolveSample1 || this.resolveSample2 || this.resolveSample3)){
            action = "LABORATORY";
        }else if (cando && countDiag(minesSamples) == minesSamples.size()) {
            if(minesSamples.size()>1 && !getPosition().equals("LABORATORY")){
                action = "MOLECULES";
            }else if(minesSamples.size()>1 && getPosition().equals("LABORATORY")){
                action = "LABORATORY";
            }else{
                action = "SAMPLES";
            }
        } else if (minesSamples.size() > 2 && countDiag(minesSamples) >1 && (this.resolveSample1 || this.resolveSample2 || this.resolveSample3) && cando) { // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            if(getPosition().equals("DIAGNOSIS") || getPosition().equals("LABORATORY")){
                action = "LABORATORY";
            }else{
                action = "SAMPLES";
            }
        } else if (!cando && countDiag(minesSamples) <= 2) { // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            action = "SAMPLES";
        } else if (minesSamples.size()==0 || (minesSamples.size()<3  && this.position.equals("SAMPLES"))) {
            action = "SAMPLES";
        } else if (countDiag(minesSamples) < minesSamples.size()) {
            action = "DIAGNOSIS";
        } else if (countDiag(minesSamples) == minesSamples.size() && getPosition().equals("LABORATORY")) {
            action = "LABORATORY";
        } else if (countDiag(minesSamples) == minesSamples.size() && (!getPosition().equals("LABORATORY") || minesSamples.size()>1)) {
            action = "MOLECULES";
        } else if(minesSamples.size()>1 && !testCanDoNow(minesSamples.get(0))&& !testCanDoNow(minesSamples.get(1))){
            action = "SAMPLES";
        } else if(minesSamples.size()>0 && !testCanDoNow(minesSamples.get(0))){
            action = "SAMPLES";
        } else {
            action = "DIAGNOSIS";
        }
        return action;
    }



    public void doAction(List<Sample> minesSamples, String action, List<Integer> avaibleMolecules) {
        orderSample(minesSamples);
        System.err.println("ACTION : " + action);
        switch (action) {
            case "SAMPLES":
                actionSamples(minesSamples);
                break;
            case "DIAGNOSIS":
                actionDiagnosis(minesSamples);
                break;
            case "MOLECULES":
                actionMolecules(minesSamples, avaibleMolecules);
                break;
            case "LABORATORY":
                //orderSampleByHealth(minesSamples);
                actionLabotory(minesSamples);
                break;
            case "CLOUD":
                actionCloud(minesSamples);
                break;
            case "WAIT":
                System.out.println("WAIT");
                break;
            default:
                System.out.println("GOTO SAMPLES");
                break;
        }
    }

    private void orderSample(List<Sample> minesSamples) {
        minesSamples.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
    }
    private void orderSampleByHealth(List<Sample> minesSamples) {
        minesSamples.sort((p1, p2) -> p2.getHealth().compareTo(p1.getHealth()));
    }


    public void actionSamples(List<Sample> minesSamples) {
        if (!this.position.equals("SAMPLES")) {
            System.out.println("GOTO SAMPLES");

        } else if (minesSamples.size() < 3) {

            int test = 0;

            for (int k:this.expertize) {
                test+=k;
            }
            if(this.escapeCloud.equals("CLOUD")){
                System.out.println("CONNECT 1");
                this.escapeCloud = "";
            } else if (test > 10) {
                System.out.println("CONNECT 3");
            } else if (test > 5) {
                System.out.println("CONNECT 2");
            } else {
                System.out.println("CONNECT 1");
            }
        } else {
            System.out.println("GOTO DIAGNOSIS");
        }
    }

    public void actionDiagnosis(List<Sample> minesSamples) {
        if (!this.position.equals("DIAGNOSIS")) {
            System.out.println("GOTO DIAGNOSIS");
        } else {
            if (minesSamples.size() > 0 && !minesSamples.get(0).isDiagnosed()) { // Si j'ai 3 samples et que le 0 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(0).getId());
            } else if (minesSamples.size() > 1 && !minesSamples.get(1).isDiagnosed()) { // Si j'ai 3 samples et que le 1 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(1).getId());
            } else if (minesSamples.size() > 2 && !minesSamples.get(2).isDiagnosed()) { // Si j'ai 3 samples et que le 2 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(2).getId());
            } else {
                System.out.println("GOTO MOLECULES");
            }
        }
    }

    public void actionCloud(List<Sample> minesSamples) {
        if (!this.position.equals("DIAGNOSIS")) {
            System.out.println("GOTO DIAGNOSIS");
        } else {
            System.out.println("CONNECT " + getHardSample(minesSamples));
            this.escapeCloud = "";
        }
    }

    public void actionMolecules(List<Sample> minesSamples, List<Integer> avaibleMolecules) {
        if (!this.position.equals("MOLECULES")) {
            System.out.println("GOTO MOLECULES");
        } else {
            Sample sample = null;
            int resolv = 0;

            //System.err.println("j'ai " + molleculeAllDispo(minesSamples.get(0), avaibleMolecules));
            //System.err.println("j'ai " + molleculeAllDispo(minesSamples.get(1), avaibleMolecules));
            //System.err.println("j'ai " + molleculeAllDispo(minesSamples.get(2), avaibleMolecules));

            if (minesSamples.size()>0 && !this.resolveSample1 && molleculeAllDispo(minesSamples.get(0), avaibleMolecules)) {
                sample = minesSamples.get(0);
                resolv = 1;
            } else if (minesSamples.size()>1 && !this.resolveSample2 && molleculeAllDispo(minesSamples.get(1), avaibleMolecules)) {
                sample = minesSamples.get(1);
                resolv = 2;
            } else if (minesSamples.size()>2 && !this.resolveSample3 && molleculeAllDispo(minesSamples.get(2), avaibleMolecules)) {
                sample = minesSamples.get(2);
                resolv = 3;
            }

            System.err.println("RESOLV :" + resolv);

            if (resolv==0 && minesSamples.size()>0 && testCanDoNow(minesSamples.get(0))) {
                this.resolveSample1 = true;
                System.err.println("1");
                System.out.println("GOTO LABORATORY");
            } else if (resolv==0 && minesSamples.size()>1 &&  testCanDoNow(minesSamples.get(1))) {
                this.resolveSample2 = true;
                System.err.println("2");
                System.out.println("GOTO LABORATORY");
            } else if (resolv==0 && minesSamples.size()>2 && testCanDoNow(minesSamples.get(2))) {
                this.resolveSample3 = true;
                System.err.println("3");
                System.out.println("GOTO LABORATORY");
            } else if (resolv > 0) {

                System.err.println("j'ai " + getTotalMolecules());

                if (sample.getRealCosts().get(0) > this.carryA && avaibleMolecules.get(0) > 0 && getTotalMolecules() < 10) {
                    System.out.println("CONNECT A");
                    this.carryA++;
                } else if (sample.getRealCosts().get(1) > this.carryB && avaibleMolecules.get(1) > 0 && getTotalMolecules() < 10) {
                    System.out.println("CONNECT B");
                    this.carryB++;
                } else if (sample.getRealCosts().get(2) > this.carryC && avaibleMolecules.get(2) > 0 && getTotalMolecules() < 10) {
                    System.out.println("CONNECT C");
                    this.carryC++;
                } else if (sample.getRealCosts().get(3) > this.carryD && avaibleMolecules.get(3) > 0 && getTotalMolecules() < 10) {
                    System.out.println("CONNECT D");
                    this.carryD++;
                } else if (sample.getRealCosts().get(4) > this.carryE && avaibleMolecules.get(4) > 0 && getTotalMolecules() < 10) {
                    System.out.println("CONNECT E");
                    this.carryE++;
                }else {
                    System.out.println("WAIT");
                }

                /*List<Molecule> molecules = new ArrayList<>();
                for (Molecule m:molecules) {
                    if (sample.getRealCosts().get(getIndexFromLetter(m.getLetter(), molecules)) > this.carryE && getTotalMolecules() < 10){
                        System.out.println("CONNECT "+m.getLetter());
                    }
                }*/

                if (resolv == 1 && testCanDoNow(sample)) {
                    this.resolveSample1 = true;
                    System.err.println("1");
                } else if (resolv == 2 && testCanDoNow(sample)) {
                    this.resolveSample2 = true;
                    System.err.println("2");
                } else if (resolv == 3 && testCanDoNow(sample)) {
                    this.resolveSample3 = true;
                    System.err.println("3");
                }/* else {
                System.out.println("GOTO MOLECULES");
            }*/
            } else {
                System.out.println("WAIT");
            }

        }
    }

    public void actionLabotory(List<Sample> minesSamples) {
        if (!this.position.equals("LABORATORY")) {
            System.out.println("GOTO LABORATORY");
        } else {
            if (this.resolveSample1) {
                System.out.println("CONNECT " + minesSamples.get(0).getId());
                reduceCarry(minesSamples.get(0));
                this.resolveSample1 = false;
                this.resolveSample2 = false;
                this.resolveSample3 = false;
            } else if (this.resolveSample2) {
                System.out.println("CONNECT " + minesSamples.get(1).getId());
                reduceCarry(minesSamples.get(1));
                this.resolveSample1 = false;
                this.resolveSample2 = false;
                this.resolveSample3 = false;
            } else if (this.resolveSample3) {
                System.out.println("CONNECT " + minesSamples.get(2).getId());
                reduceCarry(minesSamples.get(2));
                this.resolveSample1 = false;
                this.resolveSample2 = false;
                this.resolveSample3 = false;
            } else {
                if(minesSamples.size()>1){
                    System.err.println("on repasse en mode MOLECULES");
                    System.out.println("GOTO MOLECULES");
                }else{
                    System.err.println("on repasse en mode SAMPLES");
                    System.out.println("GOTO SAMPLES");
                }
            }
        }
    }

    private void reduceCarry(Sample sample) {
        this.carryA = this.carryA - sample.getRealCosts().get(0);
        this.carryB = this.carryB - sample.getRealCosts().get(1);
        this.carryC = this.carryC - sample.getRealCosts().get(2);
        this.carryD = this.carryD - sample.getRealCosts().get(3);
        this.carryE = this.carryE - sample.getRealCosts().get(4);
    }

    private boolean canTakeAllMollecule(List<Sample> minesSamples){
        boolean result = true;
        for (Sample sample:minesSamples) {

        }
        return result;
    }

    private boolean molleculeAllDispo(Sample sample, List<Integer> avaibleMolecules) {
        int good = 0;
        if (((sample.getRealCosts().get(0) <= (this.carryA + avaibleMolecules.get(0))) || (sample.getRealCosts().get(0) == this.carryA)) && getTotalMolecules() < 10) {
            good++;
        }
        if (((sample.getRealCosts().get(1) <= (this.carryB + avaibleMolecules.get(1))) || (sample.getRealCosts().get(1) == this.carryB)) && getTotalMolecules() < 10) {
            good++;
        }
        if (((sample.getRealCosts().get(2) <= (this.carryC + avaibleMolecules.get(2))) || (sample.getRealCosts().get(2) == this.carryC)) && getTotalMolecules() < 10) {
            good++;
        }
        if (((sample.getRealCosts().get(3) <= (this.carryD + avaibleMolecules.get(3))) || (sample.getRealCosts().get(3) == this.carryD)) && getTotalMolecules() < 10) {
            good++;
        }
        if (((sample.getRealCosts().get(4)) <= (this.carryE+ avaibleMolecules.get(4)) || (sample.getRealCosts().get(4) == this.carryE)) && getTotalMolecules() < 10) {
            good++;
        }
        return good == 5;
    }

    private int getIndexFromLetter(String letter, List<Molecule> molecules){
        for(int i = 0 ; i<molecules.size() ; i++)
        {
            if(molecules.get(i).getLetter().equals(letter)){
                return i;
            }
        }
        return 0;
    }

    private boolean testCanDoNow(Sample sample) {
        if (sample != null && sample.isDiagnosed()
                && sample.getRealCosts().get(0) <= this.carryA
                && sample.getRealCosts().get(1) <= this.carryB
                && sample.getRealCosts().get(2) <= this.carryC
                && sample.getRealCosts().get(3) <= this.carryD
                && sample.getRealCosts().get(4) <= this.carryE) {
            System.err.println("OUIII Je peux fabriquer ");
            return true;
        } else {
            System.err.println("NONONONONO");
            return false;
        }
    }

    private int countDiag(List<Sample> minesSamples) {
        int i = 0;
        for (Sample s : minesSamples) {
            if (s.isDiagnosed()) {
                i++;
            }
        }
        return i;
    }

    private int getHardSample(List<Sample> minesSamples){
        int i = minesSamples.get(0).getId();
        int value = minesSamples.get(0).getValue();
        for (Sample s : minesSamples) {
            if (s.isDiagnosed()) {
                if(s.getValue() <= value){
                    value = s.getValue();
                    i = s.getId();
                }
            }
        }
        return i;
    }
}

class Sample {
    private int id;
    private Integer health;
    private Integer value;
    private int totalCost;
    private int totalRealCost;
    private List<Integer> costs = new ArrayList<>();
    private List<Integer> realCosts = new ArrayList<>();
    private boolean resolve = true;


    public Sample(int id, int hp, int a, int b, int c, int d, int e, int realA, int realB, int realC, int realD, int realE) {
        this.id = id;
        this.health = hp;
        this.costs.add(a);
        this.costs.add(b);
        this.costs.add(c);
        this.costs.add(d);
        this.costs.add(e);
        this.realCosts.add(realA);
        this.realCosts.add(realB);
        this.realCosts.add(realC);
        this.realCosts.add(realD);
        this.realCosts.add(realE);
        this.totalCost = a + b + c + d + e;
        this.totalRealCost = realA + realB + realC + realD + realE;
        //this.value = (this.totalRealCost) > 0 ? hp / (this.totalRealCost) : hp;
    }

    public Integer getValue() {
        if (!this.isDiagnosed()) {
            return this.health + 100;
        } else if (this.totalRealCost > 0) {
            return this.health / this.totalRealCost;
        } else {
            return 100;
        }
    }

    public int getIntValue() {
        return (int) this.value;
    }
    public int getId() {
        return id;
    }
    public Integer getHealth() {
        return health;
    }

    public int getTotalCost() {
        return this.totalCost;
    }
    public int getTotalRealCost() {
        return this.totalRealCost;
    }

    public List<Integer> getCosts() {
        return costs;
    }

    public List<Integer> getRealCosts() {
        return realCosts;
    }

    public boolean isDiagnosed() {
        return this.totalCost>= 0;
    }

    public boolean isResolve() {
        return resolve;
    }

    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }
}

class Molecule {
    private String letter;
    private Integer value;

    public Molecule(int a, String s) {
        this.value = a;
        this.letter = s;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getLetter() {
        return this.letter;
    }
}
/*
import java.util.*;
import java.io.*;
import java.math.*;

*/
/**
 * Created by guillaumeboutin on 15/05/2017.
 *//*


*/
/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 **//*

class Player {

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
                String action = gamePlay.getAction(minesSamples);
                gamePlay.doAction(minesSamples, action, avaibleMolecules);
                gamePlay.showResolutions();
            }
        }
    }
}

class GamePlay {

    private String position;
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
    }

    public String getPosition() {
        return this.position;
    }


    public void setPosition(String s) {
        position = s;
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

    public String getAction(List<Sample> minesSamples) {
        String action;

        if (minesSamples.size()>0 && testCanTakeMollecules(minesSamples.get(0))) {
            this.resolveSample1 = true;
            System.err.println("AVANT 1");
        } else if (minesSamples.size()>1 && testCanTakeMollecules(minesSamples.get(1))) {
            this.resolveSample2 = true;
            System.err.println("AVANT 2");
        } else if (minesSamples.size()>2 &&  testCanTakeMollecules(minesSamples.get(2))) {
            this.resolveSample3 = true;
            System.err.println("AVANT 3");
        }

        // GOGOGOGOGO SUPER MACHINE STATE
        if (this.resolveSample1 || this.resolveSample2 || this.resolveSample3) { // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            action = "LABORATORY";
        } else if (!this.resolveSample1 && !this.resolveSample2 && !this.resolveSample3 && getTotalMolecules() == 10 && countDiag(minesSamples) > 0) { // ------ Si tout les samples sont résolus ou deja 10 mollécule // GO TO LABORATORY
            action = "CLOUD";
        } else if (minesSamples.size() < 3) {
            action = "SAMPLES";
        } else if (minesSamples.size() == 3 && countDiag(minesSamples) == 3) {
            action = "MOLECULES";
        } else if (minesSamples.size() == 3 && countDiag(minesSamples) < 3) {
            action = "DIAGNOSIS";
        } else {
            action = "MOLECULES";
        }
        return action;
    }


    public void doAction(List<Sample> minesSamples, String action, List<Integer> avaibleMolecules) {
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
                actionLabotory(minesSamples);
                break;
            case "CLOUD":
                actionCloud(minesSamples);
                break;
            default:
                System.out.println("GOTO SAMPLES");
                break;
        }
    }

    public void actionCloud(List<Sample> minesSamples) {
        if (!this.position.equals("DIAGNOSIS")) {
            System.out.println("GOTO DIAGNOSIS");
        } else {
            System.out.println("CONNECT " + minesSamples.get(0).getId());
            this.escapeCloud = "CLOUD";
        }
    }

    public void actionSamples(List<Sample> minesSamples) {
        if (!this.position.equals("SAMPLES")) {
            System.out.println("GOTO SAMPLES");

        } else if (minesSamples.size() < 3) {

            System.err.println("LAAAAAAAAAAAAAAAAAAAAAA");

            int test = 0;
            for (int k:this.expertize) {
                test+=k;
            }

            if (test > 8) {

                System.out.println("CONNECT 3");

            } else if (test > 4) {

                System.out.println("CONNECT 3");

            }else if(this.escapeCloud.equals("CLOUD")){

                System.out.println("CONNECT 1");
                this.escapeCloud = "";

            } else {

                System.out.println("CONNECT 2");

            }




        } else {
            System.out.println("GOTO DIAGNOSIS");
        }
    }

    public void actionDiagnosis(List<Sample> minesSamples) {
        if (!this.position.equals("DIAGNOSIS")) {
            System.out.println("GOTO DIAGNOSIS");
        } else {
            if (minesSamples.size() == 3 && !minesSamples.get(0).isDiagnosed()) { // Si j'ai 3 samples et que le 0 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(0).getId());
            } else if (minesSamples.size() == 3 && !minesSamples.get(1).isDiagnosed()) { // Si j'ai 3 samples et que le 1 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(1).getId());
            } else if (minesSamples.size() == 3 && !minesSamples.get(2).isDiagnosed()) { // Si j'ai 3 samples et que le 2 est non diagnostiqué
                System.out.println("CONNECT " + minesSamples.get(2).getId());
            } else {
                System.out.println("GOTO MOLECULES");
            }
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

            if (!this.resolveSample1 && molleculeAllDispo(minesSamples.get(0), avaibleMolecules)) {
                sample = minesSamples.get(0);
                resolv = 1;
            } else if (!this.resolveSample2 && molleculeAllDispo(minesSamples.get(1), avaibleMolecules)) {
                sample = minesSamples.get(1);
                resolv = 2;
            } else if (!this.resolveSample3 && molleculeAllDispo(minesSamples.get(2), avaibleMolecules)) {
                sample = minesSamples.get(2);
                resolv = 3;
            }

            System.err.println("RESOLV :" + resolv);

            if (resolv==0 && minesSamples.get(0) != null && testCanTakeMollecules(minesSamples.get(0))) {
                this.resolveSample1 = true;
                System.err.println("1");
                System.out.println("GOTO LABORATORY");
            } else if (resolv==0 && minesSamples.get(1) != null && testCanTakeMollecules(minesSamples.get(1))) {
                this.resolveSample2 = true;
                System.err.println("2");
                System.out.println("GOTO LABORATORY");
            } else if (resolv==0 && minesSamples.get(2) != null && testCanTakeMollecules(minesSamples.get(2))) {
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

                }

                if (resolv == 1 && testCanTakeMollecules(sample)) {
                    this.resolveSample1 = true;
                    System.err.println("1");
                } else if (resolv == 2 && testCanTakeMollecules(sample)) {
                    this.resolveSample2 = true;
                    System.err.println("2");
                } else if (resolv == 3 && testCanTakeMollecules(sample)) {
                    this.resolveSample3 = true;
                    System.err.println("3");
                }*/
/* else {
                System.out.println("GOTO MOLECULES");
            }*//*

            } else {
                System.out.println("WAIT");
            }

        }
    }

    public boolean molleculeAllDispo(Sample sample, List<Integer> avaibleMolecules) {
        int good = 0;
        //System.err.println("j'ai " + sample.getCosts().get(0)+" >= "+(this.carryA -  this.expertizeA));
        //System.err.println("j'ai " + sample.getCosts().get(1)+" >= "+(this.carryB -  this.expertizeB));
        //System.err.println("j'ai " + sample.getCosts().get(2)+" >= "+(this.carryC -  this.expertizeC));
        //System.err.println("j'ai " + sample.getCosts().get(3)+" >= "+(this.carryD -  this.expertizeD));
        //System.err.println("j'ai " + sample.getCosts().get(4)+" >= "+(this.carryE -  this.expertizeE));
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

    public void actionLabotory(List<Sample> minesSamples) {
        if (!this.position.equals("LABORATORY")) {
            System.out.println("GOTO LABORATORY");
        } else {
            if (this.resolveSample1) {
                System.out.println("CONNECT " + minesSamples.get(0).getId());
                reduceCarry(minesSamples.get(0));
                this.resolveSample1 = false;
            } else if (this.resolveSample2) {
                System.out.println("CONNECT " + minesSamples.get(1).getId());
                reduceCarry(minesSamples.get(1));
                this.resolveSample2 = false;
            } else if (this.resolveSample3) {
                System.out.println("CONNECT " + minesSamples.get(2).getId());
                reduceCarry(minesSamples.get(2));
                this.resolveSample3 = false;
            } else {
                System.err.println("on repasse en mode MOLECULES");
                System.out.println("GOTO MOLECULES");
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

    private boolean testCanTakeMollecules(Sample sample) {
        if (sample != null && sample.isDiagnosed()
                && sample.getRealCosts().get(0) <= this.carryA
                && sample.getRealCosts().get(1) <= this.carryB
                && sample.getRealCosts().get(2) <= this.carryC
                && sample.getRealCosts().get(3) <= this.carryD
                && sample.getRealCosts().get(4) <= this.carryE) {
            */
/*this.carryA -= sample.getRealCosts().get(0);
            this.carryB -= sample.getRealCosts().get(1);
            this.carryC -= sample.getRealCosts().get(2);
            this.carryD -= sample.getRealCosts().get(3);
            this.carryE -= sample.getRealCosts().get(4);*//*

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
}

class Sample {
    private int id;
    private int health;
    private float value;
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
        this.value = (this.totalRealCost) > 0 ? hp / (this.totalRealCost) : hp;
    }

    public float getValue() {
        return value;
    }

    public int getId() {
        return id;
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
        //int counts = 0;
        */
/*if(verifExpertize) {
            for (Integer i : this.costs) {
                counts += i;
            }
            for (Integer j : expertize) {
                counts += j;
            }
        }else{
            for (Integer i : this.costs) {
                counts += i;
            }
        }*//*

        */
/*counts = counts + this.costs.get(0) + expertize.get(0);
        counts = counts + this.costs.get(1) + expertize.get(1);
        counts = counts + this.costs.get(2) + expertize.get(2);
        counts = counts + this.costs.get(3) + expertize.get(3);
        counts = counts + this.costs.get(4) + expertize.get(4);*//*

        //System.err.println("COUNTS : "+counts);
        //return counts >= 0;
        return this.totalCost>= 0;
    }

    public boolean isResolve() {
        return resolve;
    }

    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }
}*/

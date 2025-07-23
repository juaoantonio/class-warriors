package br.ufpa.turnbasedgame.domain.character;

// Dependo do colega para ajustar
// import br.ufpa.turnbasedgame.domain.battle.AttackResult;

// Entidade base (ao invés de Player) para Herói ou Monstro. Tudo que participa da batalha
public abstract class Entity {

    protected String name;
    protected int hitPoints;      // Pontos de Vida (HP)
    protected int attackPower;    // Força de Ataque
    protected int defense;        // Resistência a Danos (Defesa)
    protected int dexterity;      // Destreza
    protected int speed;          // Velocidade

    // Construtor de entidades
    public Entity(String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.attackPower = attackPower;
        this.defense = defense;
        this.dexterity = dexterity;
        this.speed = speed;
    }

    // --- Getters e Setters ---
    // Métodos para acessar e modificar os atributos de forma controlada (encapsulamento).

    public String getName() {
        return name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getSpeed() {
        return speed;
    }

    // --- Métodos de Lógica de Batalha ---

    // Metodo abstrato de ataque. Cada classe tem o próprio e retorna um dano bruto.
    public abstract int performAttack(Entity target);

    // Metodo para calcular o dano que a entidade recebe. Não pode ser menor que 0.
    public void receiveDamage(int rawDamage) {
        // Calcula o dano causado, garantindo que não seja negativo.
        // Se a defesa for maior que o dano bruto, o dano causado é 0.
        int effectiveDamage = Math.max(0, rawDamage - this.defense);

        // Subtrai o dano causado da vida, garantindo que o HP não fique abaixo de 0.
        this.hitPoints = Math.max(0, this.hitPoints - effectiveDamage);
    }

    // Verifica se a entidade está viva enquanto o HP for maior que 0.
    public boolean isAlive() {
        return this.hitPoints > 0;
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d, ATK: %d, DEF: %d)",
                this.name, this.hitPoints, this.attackPower, this.defense);
    }
}
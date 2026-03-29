-- CreateTable
CREATE TABLE "Hero" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "rank" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "tier" TEXT NOT NULL,
    "power" INTEGER NOT NULL,
    "atk" INTEGER NOT NULL,
    "def" INTEGER NOT NULL,
    "hp" INTEGER NOT NULL,
    "spd" INTEGER NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "Hero_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Skill" (
    "id" SERIAL NOT NULL,
    "heroId" INTEGER NOT NULL,
    "type" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "priority" INTEGER NOT NULL,
    "description" TEXT NOT NULL,
    "cooldown" DOUBLE PRECISION,
    "target" TEXT NOT NULL,
    "damageType" TEXT,
    "keyStats" TEXT[],
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "Skill_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "SkillEffect" (
    "id" SERIAL NOT NULL,
    "skillId" INTEGER NOT NULL,
    "level" INTEGER NOT NULL,
    "statKey" TEXT NOT NULL,
    "value" DOUBLE PRECISION,
    "boolValue" BOOLEAN,

    CONSTRAINT "SkillEffect_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "Hero_name_key" ON "Hero"("name");

-- AddForeignKey
ALTER TABLE "Skill" ADD CONSTRAINT "Skill_heroId_fkey" FOREIGN KEY ("heroId") REFERENCES "Hero"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "SkillEffect" ADD CONSTRAINT "SkillEffect_skillId_fkey" FOREIGN KEY ("skillId") REFERENCES "Skill"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

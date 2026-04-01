type Hero = {
  id: number;
  name: string;
  rank: number;
  type: string;
  tier: number;
  power: number;
  atk: number;
  def: number;
  hp: number;
  spd: number;
};
type Skill = {
  id: number;
  heroId: number;
  name: string;
  type: string;
  priority: number;
  description: string;
  cooldown: number | null;
  target: string;
  damageType: string | null;
  keyStats: string[] | null;
};

export interface HeroWithSkills extends Hero {
  skills: Skill[];
}
[];

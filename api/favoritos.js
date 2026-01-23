import { MongoClient } from "mongodb";

const uri = process.env.MONGODB_URI;
const client = new MongoClient(uri);

export default async function handler(req, res) {
  if (!req.body.user) return res.status(400).json({ error: "Missing user" });

  await client.connect();
  const db = client.db("claudplay");
  const favs = db.collection("favoritos");

  if (req.method === "GET") {
    const favoritos = await favs.find({ user: req.body.user }).toArray();
    return res.status(200).json({ favoritos });
  }

  if (req.method === "POST") {
    const { user, link } = req.body;
    await favs.updateOne(
      { user },
      { $addToSet: { links: link } },
      { upsert: true }
    );
    return res.status(200).json({ ok: true });
  }

  if (req.method === "DELETE") {
    const { user, link } = req.body;
    await favs.updateOne(
      { user },
      { $pull: { links: link } }
    );
    return res.status(200).json({ ok: true });
  }

  return res.status(405).json({ error: "Method not allowed" });
}
